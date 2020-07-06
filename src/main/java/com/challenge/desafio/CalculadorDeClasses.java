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
    private boolean existeAnnotationNoAtributoBigDecimal;

    public CalculadorDeClasses() {
        this.existeAnnotationNoAtributoBigDecimal = false;
    }

    private boolean validarAtributoComAnnotattion(Field atributo, Class<? extends Annotation> anotacao) {
        return atributo.isAnnotationPresent(anotacao) && atributo.getType().equals(BigDecimal.class);
    }

    private BigDecimal obterCalculoAtributosBigDecimalDaClasse(Object objeto, Class<? extends Annotation> anotacao) throws IllegalAccessException {
        this.existeAnnotationNoAtributoBigDecimal = false;
        List<BigDecimal> numerosBigDecimal = new ArrayList<>();

        if (objeto == null)
            throw new NullPointerException();

        for (Field atributo : objeto.getClass().getDeclaredFields()) {
            if (this.validarAtributoComAnnotattion(atributo, anotacao)) {
                this.existeAnnotationNoAtributoBigDecimal = true;
                this.preencherListaValores(objeto, atributo, numerosBigDecimal);
            }
        }
        return this.obterResultadoOperacao(anotacao, numerosBigDecimal);
    }

    private void preencherListaValores(Object objeto, Field atributo,  List<BigDecimal> numerosBigDecimal) throws IllegalAccessException {
        atributo.setAccessible(true);
        numerosBigDecimal.add(atributo.get(objeto) != null ? new BigDecimal(atributo.get(objeto).toString()) : BigDecimal.ZERO);
    }

    private BigDecimal obterResultadoOperacao(Class<? extends Annotation> anotacao,  List<BigDecimal> numerosBigDecimal) {
        BigDecimal valorInicial = numerosBigDecimal.get(0);
        return !this.existeAnnotationNoAtributoBigDecimal ? BigDecimal.ZERO :
                (anotacao.equals(Somar.class) ? numerosBigDecimal.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                        : numerosBigDecimal.subList(1, numerosBigDecimal.size()).stream().reduce(valorInicial, BigDecimal::subtract));
    }

    @Override
    public BigDecimal somar(Object objeto) throws IllegalAccessException {
        return this.obterCalculoAtributosBigDecimalDaClasse(objeto, Somar.class);
    }

    @Override
    public BigDecimal subtrair(Object objeto) throws IllegalAccessException {
        return this.obterCalculoAtributosBigDecimalDaClasse(objeto, Subtrair.class);
    }

    @Override
    public BigDecimal totalizar(Object objeto) throws IllegalAccessException {
        return this.somar(objeto).subtract(this.subtrair(objeto));
    }
}
