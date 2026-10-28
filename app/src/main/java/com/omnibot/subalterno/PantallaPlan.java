package com.omnibot.subalterno;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Plan de estudio adaptado a los días que quedan hasta el examen. */
final class PantallaPlan extends LinearLayout {

    PantallaPlan(MainActivity a) {
        super(a);
        setOrientation(VERTICAL);
        setBackgroundColor(Ui.BG);

        addView(a.barraSuperior("Plan de estudio", true), new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ScrollView sc = new ScrollView(a);
        sc.setClipToPadding(false);
        LinearLayout col = new LinearLayout(a);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 30));
        sc.addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(sc, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        long dias = Progreso.diasHasta(a.progreso.fechaExamen());

        // Cabecera
        LinearLayout hero = new LinearLayout(a);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setBackground(Ui.gradiente(0xFF0F172A, 0xFF1E3A8A, 18, a));
        hero.setPadding(Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 18));

        hero.addView(Ui.tv(a, "Te quedan", 11, 0x99FFFFFF, false));
        TextView d = Ui.tv(a, dias + " días", 32, 0xFFFFFFFF, true);
        d.setIncludeFontPadding(false);
        hero.addView(d);
        hero.addView(Ui.tv(a, "Fecha del examen: " + a.progreso.fechaExamen()
            + " · cambia la fecha en Ajustes", 10, 0x99FFFFFF, false));

        hero.addView(Ui.espacio(a, 14));
        int pct = Math.round(Math.min(100, a.progreso.totalDominadas() * 100f
            / Math.max(1, Banco.todas().size())));
        hero.addView(Ui.barra(a, pct, 0xFF60A5FA));
        TextView pctTv = Ui.tv(a, pct + "% del banco dominado", 10, 0xCCFFFFFF, false);
        pctTv.setPadding(0, Ui.dps(a, 7), 0, 0);
        hero.addView(pctTv);

        col.addView(hero);
        col.addView(Ui.espacio(a, 16));

        // Fases
        int faseActiva = dias > 45 ? 0 : (dias > 21 ? 1 : (dias > 7 ? 2 : 3));
        String[][] fases = {
            {"Fase 1 · Cimientos", "45+ días",
             "Lee el temario tema por tema y haz tests de 10 preguntas. "
             + "Objetivo: vistas todas las preguntas al menos una vez."},
            {"Fase 2 · Consolidación", "21-45 días",
             "Repasa los temas con menor porcentaje. Alterna un día de tema nuevo "
             + "con un día de repaso de fallos."},
            {"Fase 3 · Simulacros", "8-21 días",
             "Un simulacro completo cada 2 días. Analiza cada fallo y añádelo a repaso."},
            {"Fase 4 · Recta final", "Última semana",
             "Solo repaso de fallos y simulacros cortos. No intentes aprender materia nueva."}
        };

        col.addView(Ui.tv(a, "Tu plan por fases", 14, Ui.TEXT, true));
        col.addView(Ui.espacio(a, 10));

        for (int i = 0; i < fases.length; i++) {
            boolean activa = i == faseActiva;
            LinearLayout card = new LinearLayout(a);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackground(activa
                ? Ui.borde(0xFFEFF6FF, Ui.ACCENT, 16, 2, a)
                : Ui.redondeado(Ui.CARD, 16, a));
            card.setPadding(Ui.dps(a, 14), Ui.dps(a, 14), Ui.dps(a, 14), Ui.dps(a, 14));
            LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            clp.setMargins(0, 0, 0, Ui.dps(a, 10));
            card.setLayoutParams(clp);

            LinearLayout cab = Ui.fila(a);
            cab.addView(Ui.tv(a, fases[i][0], 13, activa ? Ui.ACCENT : Ui.TEXT, true),
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            if (activa) cab.addView(Ui.pildora(a, "AHORA", Ui.ACCENT, 0xFFFFFFFF));
            else cab.addView(Ui.tv(a, fases[i][1], 10, Ui.MUTED, false));
            card.addView(cab);

            TextView txt = Ui.tv(a, fases[i][2], 11, Ui.MUTED, false);
            txt.setPadding(0, Ui.dps(a, 7), 0, 0);
            txt.setLineSpacing(0, 1.4f);
            card.addView(txt);
            col.addView(card);
        }

        // Semana tipo
        col.addView(Ui.espacio(a, 8));
        col.addView(Ui.tv(a, "Semana tipo", 14, Ui.TEXT, true));
        col.addView(Ui.espacio(a, 10));

        LinearLayout sem = Ui.tarjeta(a);
        String[][] semana = {
            {"Lunes", "Tema nuevo + 10 preguntas"},
            {"Martes", "Repaso de fallos (20 preguntas)"},
            {"Miércoles", "Tema nuevo + 10 preguntas"},
            {"Jueves", "Repaso de fallos (20 preguntas)"},
            {"Viernes", "Simulacro corto (15 preguntas)"},
            {"Sábado", "Simulacro completo o repaso libre"},
            {"Domingo", "Descanso activo: repaso ligero 5 min"}
        };
        for (String[] s : semana) {
            LinearLayout f = Ui.fila(a);
            f.setPadding(0, Ui.dps(a, 8), 0, Ui.dps(a, 8));
            TextView dia = Ui.tv(a, s[0], 12, Ui.TEXT, true);
            dia.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(a, 80),
                ViewGroup.LayoutParams.WRAP_CONTENT));
            f.addView(dia);
            f.addView(Ui.tv(a, s[1], 11, Ui.MUTED, false),
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            sem.addView(f);
        }
        col.addView(sem);
        col.addView(Ui.espacio(a, 16));

        col.addView(Ui.boton(a, "Empezar el bloque de hoy", Ui.ACCENT, 0xFFFFFFFF, () -> {
            java.util.List<Banco.Pregunta> sel = a.progreso.seleccionRepaso(20);
            a.navegar(new PantallaTest(a, sel, "Bloque de hoy", 0, false));
        }), Ui.full(a));
    }
}
