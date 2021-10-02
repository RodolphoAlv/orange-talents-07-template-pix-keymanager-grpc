package com.zup.edu.chave

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository

@Repository
interface ChavePixRepository: JpaRepository<ChavePix, Int> {
    fun findByValorChave(valorChave: String): ChavePix?
}