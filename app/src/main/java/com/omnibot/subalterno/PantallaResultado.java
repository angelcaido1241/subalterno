package com.omnibot.subalterno;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Resultado de una sesión con desglose y acción de repetir. */
final class PantallaResultado extends LinearLayout {

    PantallaResultado(MainActivity a, int aciertos, int fallos, int sinResponder,
                      String titulo, int tema, int minutos, boolean simulacro) {
        super(a);
        setOrientation(VERTICAL);
        setBackgroundColor(Ui.BG);

        int total = aciertos + fallos + sinResponder;
        int pct = total == 0 ? 0 : Math.round(aciertos * 100f / total);

        ScrollView sc = new ScrollView(a);
        sc.setClipToPadding(false);
        LinearLayout col = new LinearLayout(a);
        col.setOrientation(VERTICAL);
        col.setPadding(Ui.dps(a, 18), Ui.dps(a, 34), Ui.dps(a, 18), Ui.dps(a, 30));
        sc.addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(sc, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // Círculo con nota
        int color = pct >= 80 ? Ui.OK : (pct >= 50 ? Ui.WARN : Ui.KO);
        String mensaje = pct >= 90 ? "¡Excelente!" : pct >= 75 ? "Muy bien"
            : pct >= 50 ? "Vas por buen camino" : "Necesitas repasar";

        LinearLayout hero = new LinearLayout(a);
        hero.setOrientation(VERTICAL);
        hero.setGravity(Gravity.CENTER);
        hero.setPadding(0, Ui.dps(a, 10), 0, Ui.dps(a, 24));

        TextView pctTv = Ui.tv(a, pct + "%", 58, color, true);
        pctTv.setGravity(Gravity.CENTER);
        pctTv.setIncludeFontPadding(false);
        hero.addView(pctTv);

        TextView msg = Ui.tv(a, mensaje, 15, Ui.TEXT, true);
        msg.setGravity(Gravity.CENTER);
        msg.setPadding(0, Ui.dps(a, 8), 0, 0);
        hero.addView(msg);

        TextView sub = Ui.tv(a, (simulacro ? "Simulacro · " : "Test · ") + titulo
            + "  ·  " + minutos + " min", 11, Ui.MUTED, false);
        sub.setGravity(Gravity.CENTER);
        sub.setPadding(0, Ui.dps(a, 6), 0, 0);
        hero.addView(sub);
        col.addView(hero);

        // Desglose
        LinearLayout cards = Ui.fila(a);
        cards.addView(card(String.valueOf(aciertos), "acertadas", Ui.OK, 0xFFF0FDF4),
            peso(a, 0, 10));
        cards.addView(card(String.valueOf(fallos), "falladas", Ui.KO, 0xFFFEF2F2),
            peso(a, 10, 10));
        cards.addView(card(String.valueOf(sinResponder), "sin responder", Ui.MUTED, 0xFFF1F5F9),
            peso(a, 10, 0));
        col.addView(cards);

        col.addView(Ui.espacio(a, 22));

        // Acciones
        col.addView(Ui.boton(a, "Volver al inicio", Ui.PRIMARY, 0xFFFFFFFF, () -> {
            a.volver();
            a.irTab(MainActivity.TAB_INICIO);
        }), Ui.full(a));

        col.addView(Ui.espacio(a, 10));

        if (fallos > 0) {
            col.addView(Ui.boton(a, "Repasar las " + fallos + " falladas ahora",
                0xFFFFFFFF, Ui.KO, () -> {
                java.util.List<Banco.Pregunta> f = new java.util.ArrayList<>();
                java.util.List<Banco.Pregunta> pool =
                    tema > 0 ? Banco.delTema(tema) : Banco.todas();
                for (Banco.Pregunta q : pool)
                    if (a.progreso.fallos(q) > 0 && !a.progreso.dominada(q)) f.add(q);
                if (f.isEmpty()) f = a.progreso.preguntasFalladas();
                if (f.isEmpty()) f = a.progreso.seleccionAleatoria(tema, 10);
                a.mostrar(new PantallaTest(a, f, "Refuerzo", tema, false));
            }), Ui.full(a));
        }

        col.addView(Ui.espacio(a, 10));
        col.addView(Ui.boton(a, "Hacer otro test", 0xFFFFFFFF, Ui.PRIMARY, () -> {
            a.mostrar(new PantallaTest(a,
                a.progreso.seleccionAleatoria(tema, 10),
                titulo, tema, simulacro));
        }), Ui.full(a));
    }

    private LinearLayout.LayoutParams peso(MainActivity a, int izq, int der) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p.setMargins(Ui.dps(a, izq), 0, Ui.dps(a, der), 0);
        return p;
    }

    private LinearLayout card(String valor, String etiqueta, int color, int fondo) {
        LinearLayout l = new LinearLayout(getContext());
        l.setOrientation(VERTICAL);
        l.setGravity(Gravity.CENTER);
        l.setBackground(Ui.redondeado(fondo, 16, getContext()));
        l.setPadding(0, Ui.dps(getContext(), 16), 0, Ui.dps(getContext(), 16));
        TextView v = Ui.tv(getContext(), valor, 24, color, true);
        v.setGravity(Gravity.CENTER);
        TextView e = Ui.tv(getContext(), etiqueta, 10, Ui.MUTED, false);
        e.setGravity(Gravity.CENTER);
        e.setPadding(0, Ui.dps(getContext(), 3), 0, 0);
        l.addView(v);
        l.addView(e);
        return l;
    }
}
