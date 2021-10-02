package com.zup.edu.chave

import java.util.*

enum class TipoChave {
    UNKNOWN_KEY {
        override fun valida(valor: String): Boolean {
            return false
        }
    },
    CPF {
        override fun valida(valor: String): Boolean {
            return valor.matches("^[0-9]{11}\$".toRegex())
        }
    },
    CELULAR {
        override fun valida(valor: String): Boolean {
            return valor.matches("^\\+[1-9][0-9]\\d{1,14}\$".toRegex())
        }
    },
    CHAVE_ALEATORIA {
        override fun valida(valor: String): Boolean {
            return true
        }
    };

    abstract fun valida(valor: String): Boolean;
}