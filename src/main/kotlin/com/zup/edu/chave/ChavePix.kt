package com.zup.edu.chave

import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
class ChavePix(
    @field:NotBlank
    val idCliente: String,

    @field:Enumerated(EnumType.STRING)
    @field:NotNull
    val tipoChave: TipoChave,

    @field:Enumerated(EnumType.STRING)
    @field:NotNull
    val tipoConta: TipoConta
) {
    @Id
    @GeneratedValue
    var id: Int? = null
}