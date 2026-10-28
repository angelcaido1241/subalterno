package com.omnibot.subalterno;

import java.util.ArrayList;
import java.util.List;

/**
 * Banco de preguntas extraído de los exámenes oficiales de preparación
 * (examen tema 1..9 + examen global 1) de la convocatoria 81/26.
 */
public final class Banco {

    public static final class Pregunta {
        public final int tema;
        public final String enunciado;
        public final String[] opciones;
        public final int correcta;
        public final String explicacion;

        Pregunta(int tema, String enunciado, String a, String b, String c,
                 int correcta, String explicacion) {
            this.tema = tema;
            this.enunciado = enunciado;
            this.opciones = new String[]{a, b, c};
            this.correcta = correcta;
            this.explicacion = explicacion;
        }

        public String id() {
            return tema + "-" + Integer.toHexString(enunciado.hashCode());
        }
    }

    private static final List<Pregunta> TODAS = new ArrayList<>();

    private static void p(int tema, String en, String a, String b, String c, int ok, String exp) {
        TODAS.add(new Pregunta(tema, en, a, b, c, ok, exp));
    }

    public static List<Pregunta> todas() { return TODAS; }

    public static List<Pregunta> delTema(int tema) {
        List<Pregunta> out = new ArrayList<>();
        for (Pregunta q : TODAS) if (q.tema == tema) out.add(q);
        return out;
    }

    public static Pregunta porId(String id) {
        for (Pregunta q : TODAS) if (q.id().equals(id)) return q;
        return null;
    }

    static {
        BancoT1.cargar(Banco::p);
        BancoT2T3.cargar(Banco::p);
        BancoT4T5.cargar(Banco::p);
        BancoT6.cargar(Banco::p);
        BancoT7.cargar(Banco::p);
        BancoT8.cargar(Banco::p);
        BancoT9.cargar(Banco::p);
    }
}
