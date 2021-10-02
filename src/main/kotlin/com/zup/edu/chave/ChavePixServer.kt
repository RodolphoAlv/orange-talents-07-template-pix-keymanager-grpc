package com.zup.edu.chave

import com.zup.edu.chavePix.ChavePixRequest
import com.zup.edu.chavePix.ChavePixResponse
import com.zup.edu.chavePix.ChavePixServiceGrpc
import io.grpc.Status
import io.grpc.StatusRuntimeException
import io.grpc.stub.StreamObserver
import jakarta.inject.Singleton

@Singleton
class ChavePixServer(
    val repository: ChavePixRepository
): ChavePixServiceGrpc.ChavePixServiceImplBase() {

    override fun cadastraChave(
        request: ChavePixRequest?,
        responseObserver: StreamObserver<ChavePixResponse>?
    ) {

        val idClienteInvalido = request!!.idCliente.isBlank()

        if(idClienteInvalido) {

            with(responseObserver!!) {
               onError(
                   Status.INVALID_ARGUMENT
                       .withDescription("O id do cliente deve ser preenchido!")
                       .asRuntimeException()
               )
           }
        }

        val tipoChave: TipoChave = try {
            TipoChave.valueOf(request!!.tipoChave.toString())
        } catch (e: IllegalArgumentException) {
            TipoChave.UNKNOWN_KEY
        }

        if(tipoChave == TipoChave.UNKNOWN_KEY) {
            with(responseObserver!!) {
                onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("Tipo de chave Pix desconhecido!")
                        .asRuntimeException()
                )
                onCompleted()
            }
            return
        }

        val tipoConta: TipoConta = try {
            TipoConta.valueOf(request!!.tipoConta.toString())
        } catch (e: IllegalArgumentException) {
            TipoConta.UNKNOWN_ACCOUNT
        }

        if(tipoConta == TipoConta.UNKNOWN_ACCOUNT) {
            with(responseObserver!!) {
                onError(
                    Status.INVALID_ARGUMENT
                        .withDescription("Tipo de conta desconhecido!")
                        .asRuntimeException()
                )
                onCompleted()
                return
            }
        }

        val chaveValida: Boolean = tipoChave.valida(request!!.valorChave)

        if(!chaveValida) {
            with(responseObserver!!) {
                onError(
                    Status.FAILED_PRECONDITION
                        .withDescription("Chave inválida")
                        .asRuntimeException()
                )
                onCompleted()
            }
            return
        }

        val chavePix: ChavePix = ChavePix(request.idCliente, tipoChave, tipoConta)
        chavePix.valorChave = request.valorChave

        repository.findByValorChave(chavePix.valorChave!!)?.let {
            with(responseObserver!!) {
                onError(
                    Status.ALREADY_EXISTS
                        .withDescription("A chave já existe!")
                        .asRuntimeException()
                )
                responseObserver.onCompleted()
            }
            return
        }

        repository.save(chavePix)

        with(responseObserver!!) {
            onNext(
                ChavePixResponse.newBuilder()
                    .setPixId(chavePix.id!!)
                    .build()
            )
            onCompleted()
        }
    }
}