package com.zup.edu.chave

import com.zup.edu.chavePix.ChavePixRequest
import com.zup.edu.chavePix.ChavePixResponse
import com.zup.edu.chavePix.ChavePixServiceGrpc
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

        var tipoChave: TipoChave = try {
            TipoChave.valueOf(request!!.tipoChave.toString())
        } catch (e: IllegalArgumentException) {
            TipoChave.UNKNOWN_KEY
        }
    }
}