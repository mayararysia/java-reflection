package com.challenge.desafio;

import com.challenge.annotation.Somar;
import com.challenge.annotation.Subtrair;
import com.challenge.interfaces.Calculavel;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class CalculadorDeClasses implements Calculavel {
    @Override
    public BigDecimal somar(Object objeto) throws IllegalAccessException {
        LogicaCalculadorSoma logicaCalculadorSoma = new LogicaCalculadorSoma();
        return logicaCalculadorSoma.obterCalculoAtributosBigDecimalDaClasse(objeto, Somar.class);
    }

    @Override
    public BigDecimal subtrair(Object objeto) throws IllegalAccessException {
        LogicaCalculadorSoma logicaCalculadorSoma = new LogicaCalculadorSoma();
        return logicaCalculadorSoma.obterCalculoAtributosBigDecimalDaClasse(objeto, Subtrair.class);
    }

    @Override
    public BigDecimal totalizar(Object objeto) throws IllegalAccessException {
        return this.somar(objeto).subtract(this.subtrair(objeto));
    }
}
