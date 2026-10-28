package com.omnibot.subalterno;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Detalle de un tema: opciones de práctica y lista de preguntas. */
final class PantallaTema extends LinearLayout {

    private final MainActivity act;
    private final int tema;

    PantallaTema(MainActivity a, int tema) {
        super(a);
        this.act = a;
        this.tema = tema;
        setOrientation(VERTICAL);
        setBackgroundColor(Ui.BG);

        addView(act.barraSuperior("Tema " + tema, true), new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ScrollView sc = new ScrollView(a);
        sc.setClipToPadding(false);
        LinearLayout col = new LinearLayout(a);
        col.setOrientation(VERTICAL);
        col.setPadding(Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 30));
        sc.addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(sc, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        // Cabecera
        String titulo = Temas.NOMBRES[tema].replace("Tema " + tema + " · ", "");
        TextView t = Ui.tv(a, titulo, 22, Ui.TEXT, true);
        t.setLineSpacing(0, 1.1f);
        col.addView(t);
        col.addView(Ui.espacio(a, 6));
        TextView res = Ui.tv(a, Temas.RESUMEN[tema], 12, Ui.MUTED, false);
        res.setLineSpacing(0, 1.3f);
        col.addView(res);
        col.addView(Ui.espacio(a, 16));

        int total = Banco.delTema(tema).size();
        int dominadas = 0;
        for (Banco.Pregunta q : Banco.delTema(tema)) if (a.progreso.dominada(q)) dominadas++;

        LinearLayout stats = Ui.fila(a);
        stats.addView(stat(String.valueOf(total), "preguntas"), peso(0, 8));
        stats.addView(stat(String.valueOf(dominadas), "dominadas"), peso(8, 8));
        stats.addView(stat(a.progreso.porTema()[tema][3] + "%", "acierto"), peso(8, 0));
        col.addView(stats);
        col.addView(Ui.espacio(a, 18));

        // Acciones
        col.addView(opcion("📝", "Test de " + Math.min(10, total) + " preguntas",
            "Sin tiempo, con corrección al instante", () -> {
                act.navegar(new PantallaTest(act,
                    a.progreso.seleccionAleatoria(tema, 10), titulo, tema, false));
            }));
        col.addView(Ui.espacio(a, 10));
        col.addView(opcion("⏱️", "Simulacro del tema",
            "Cronometrado, " + Math.min(20, total) + " preguntas", () -> {
                act.navegar(new PantallaTest(act,
                    a.progreso.seleccionAleatoria(tema, Math.min(20, total)),
                    "Simulacro · " + titulo, tema, true));
            }));
        col.addView(Ui.espacio(a, 10));
        col.addView(opcion("🔁", "Repasar fallos del tema",
            "Solo las que has fallado antes", () -> {
                java.util.List<Banco.Pregunta> f = new java.util.ArrayList<>();
                for (Banco.Pregunta q : a.progreso.preguntasFalladas())
                    if (q.tema == tema) f.add(q);
                if (f.isEmpty()) {
                    toast("Aún no tienes fallos en este tema 🎉");
                } else {
                    act.navegar(new PantallaTest(act, f, "Fallos · " + titulo, tema, false));
                }
            }));

        col.addView(Ui.espacio(a, 22));
        col.addView(Ui.tv(a, "Todas las preguntas del tema", 14, Ui.TEXT, true));
        col.addView(Ui.espacio(a, 10));

        int n = 1;
        for (Banco.Pregunta q : Banco.delTema(tema)) {
            col.addView(filaPregunta(q, n++));
        }
    }

    private LinearLayout.LayoutParams peso(int izq, int der) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p.setMargins(Ui.dps(act, izq), 0, Ui.dps(act, der), 0);
        return p;
    }

    private LinearLayout stat(String valor, String etiqueta) {
        LinearLayout l = new LinearLayout(act);
        l.setOrientation(VERTICAL);
        l.setGravity(Gravity.CENTER);
        l.setBackground(Ui.redondeado(Ui.CARD, 14, act));
        l.setPadding(0, Ui.dps(act, 12), 0, Ui.dps(act, 12));
        TextView v = Ui.tv(act, valor, 18, Ui.PRIMARY, true);
        v.setGravity(Gravity.CENTER);
        TextView e = Ui.tv(act, etiqueta, 10, Ui.MUTED, false);
        e.setGravity(Gravity.CENTER);
        l.addView(v);
        l.addView(e);
        return l;
    }

    private LinearLayout opcion(String emoji, String titulo, String sub, Runnable r) {
        LinearLayout l = Ui.fila(act);
        l.setBackground(Ui.ripple(Ui.redondeado(Ui.CARD, 16, act), 0x14000000));
        l.setPadding(Ui.dps(act, 14), Ui.dps(act, 14), Ui.dps(act, 14), Ui.dps(act, 14));
        l.addView(Ui.circulo(act, 0xFFEFF6FF, 38, emoji));
        LinearLayout mid = new LinearLayout(act);
        mid.setOrientation(VERTICAL);
        mid.setPadding(Ui.dps(act, 12), 0, 0, 0);
        mid.addView(Ui.tv(act, titulo, 13, Ui.TEXT, true));
        TextView s = Ui.tv(act, sub, 10, Ui.MUTED, false);
        s.setPadding(0, Ui.dps(act, 2), 0, 0);
        mid.addView(s);
        l.addView(mid, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView flecha = Ui.tv(act, "›", 20, Ui.MUTED, false);
        l.addView(flecha);
        l.setOnClickListener(v -> r.run());
        return l;
    }

    private LinearLayout filaPregunta(Banco.Pregunta q, int n) {
        LinearLayout l = Ui.fila(act);
        l.setBackground(Ui.ripple(Ui.redondeado(Ui.CARD, 12, act), 0x10000000));
        l.setPadding(Ui.dps(act, 12), Ui.dps(act, 10), Ui.dps(act, 12), Ui.dps(act, 10));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, Ui.dps(act, 6));
        l.setLayoutParams(lp);

        TextView num = Ui.tv(act, String.valueOf(n), 11, Ui.MUTED, true);
        num.setGravity(Gravity.CENTER);
        num.setBackground(Ui.redondeado(0xFFF1F5FB, 8, act));
        num.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(act, 26), Ui.dps(act, 26)));
        l.addView(num);

        String en = q.enunciado;
        if (en.length() > 78) en = en.substring(0, 78) + "…";
        TextView txt = Ui.tv(act, en, 12, Ui.TEXT, false);
        txt.setPadding(Ui.dps(act, 10), 0, Ui.dps(act, 8), 0);
        txt.setLineSpacing(0, 1.2f);
        l.addView(txt, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        boolean vista = act.progreso.vista(q);
        boolean ok = vista && act.progreso.fallos(q) == 0;
        TextView estado = Ui.tv(act, !vista ? "·" : (ok ? "✓" : "✗"), 13,
            !vista ? Ui.MUTED : (ok ? Ui.OK : Ui.KO), true);
        l.addView(estado);

        l.setOnClickListener(v -> act.navegar(
            new PantallaTest(act, java.util.Collections.singletonList(q), "Pregunta", q.tema, false)));
        return l;
    }

    private void toast(String s) {
        android.widget.Toast.makeText(getContext(), s, android.widget.Toast.LENGTH_SHORT).show();
    }
}
