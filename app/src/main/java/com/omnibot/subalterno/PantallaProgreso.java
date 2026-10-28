package com.omnibot.subalterno;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Pantalla de progreso: estadísticas globales y por tema. */
final class PantallaProgreso extends ScrollView {

    PantallaProgreso(MainActivity a) {
        super(a);
        setBackgroundColor(Ui.BG);
        setClipToPadding(false);

        Progreso pr = a.progreso;
        int total = Banco.todas().size();

        LinearLayout col = new LinearLayout(a);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(Ui.dps(a, 18), Ui.dps(a, 26), Ui.dps(a, 18), Ui.dps(a, 30));
        addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        col.addView(Ui.titulo(a, "Tu progreso"));
        col.addView(Ui.subtitulo(a, "Seguimiento real de lo que llevas estudiado"));
        col.addView(Ui.espacio(a, 16));

        // Tarjeta principal
        LinearLayout hero = new LinearLayout(a);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setBackground(Ui.gradiente(0xFF1E3A8A, 0xFF2563EB, 18, a));
        hero.setPadding(Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 18), Ui.dps(a, 18));

        LinearLayout h1 = Ui.fila(a);
        LinearLayout izq = new LinearLayout(a);
        izq.setOrientation(LinearLayout.VERTICAL);
        TextView pct = Ui.tv(a, pr.porcentajeGlobal() + "%", 34, 0xFFFFFFFF, true);
        pct.setIncludeFontPadding(false);
        izq.addView(pct);
        izq.addView(Ui.tv(a, "acierto global", 11, 0xB3FFFFFF, false));
        h1.addView(izq, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        LinearLayout der = new LinearLayout(a);
        der.setOrientation(LinearLayout.VERTICAL);
        der.setGravity(Gravity.END);
        TextView racha = Ui.tv(a, "🔥 " + pr.racha(), 22, 0xFFFFFFFF, true);
        racha.setGravity(Gravity.END);
        der.addView(racha);
        TextView rachaLbl = Ui.tv(a, "días seguidos", 11, 0xB3FFFFFF, false);
        rachaLbl.setGravity(Gravity.END);
        der.addView(rachaLbl);
        h1.addView(der);
        hero.addView(h1);

        hero.addView(Ui.espacio(a, 16));

        LinearLayout h2 = Ui.fila(a);
        h2.addView(metrico(String.valueOf(pr.totalVistas()), "vistas"), peso(a, 0, 8));
        h2.addView(metrico(String.valueOf(pr.totalDominadas()), "dominadas"), peso(a, 8, 8));
        h2.addView(metrico(String.valueOf(total), "en total"), peso(a, 8, 0));
        hero.addView(h2);

        col.addView(hero);
        col.addView(Ui.espacio(a, 16));

        // Resumen de actividad
        LinearLayout act = Ui.tarjeta(a);
        act.addView(Ui.tv(a, "Actividad", 14, Ui.TEXT, true));
        act.addView(Ui.espacio(a, 12));
        act.addView(filaDato("Tests completados", String.valueOf(pr.testsHechos())));
        act.addView(filaDato("Simulacros", String.valueOf(pr.simulacrosHechos())));
        act.addView(filaDato("Minutos estudiados", String.valueOf(pr.minutosTotales())));
        act.addView(filaDato("Pendientes de repaso hoy", String.valueOf(pr.pendientesHoy())));
        act.addView(filaDato("Preguntas con fallos activos",
            String.valueOf(pr.preguntasFalladas().size())));
        col.addView(act);

        col.addView(Ui.espacio(a, 18));
        col.addView(Ui.tv(a, "Acierto por tema", 14, Ui.TEXT, true));
        col.addView(Ui.espacio(a, 10));

        int[][] r = pr.porTema();
        for (int t = 1; t <= 9; t++) {
            LinearLayout card = new LinearLayout(a);
            card.setOrientation(LinearLayout.VERTICAL);
            card.setBackground(Ui.redondeado(Ui.CARD, 14, a));
            card.setPadding(Ui.dps(a, 14), Ui.dps(a, 12), Ui.dps(a, 14), Ui.dps(a, 12));
            LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            clp.setMargins(0, 0, 0, Ui.dps(a, 8));
            card.setLayoutParams(clp);

            LinearLayout cab = Ui.fila(a);
            String corto = Temas.NOMBRES[t].replace("Tema " + t + " · ", "");
            cab.addView(Ui.tv(a, "T" + t, 12, Ui.ACCENT, true),
                new LinearLayout.LayoutParams(Ui.dps(a, 26),
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            cab.addView(Ui.tv(a, corto, 12, Ui.TEXT, true),
                new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
            String txt = r[t][0] == 0 ? "sin datos" : r[t][3] + "%";
            int col2 = r[t][0] == 0 ? Ui.MUTED
                : (r[t][3] >= 80 ? Ui.OK : (r[t][3] >= 60 ? Ui.WARN : Ui.KO));
            cab.addView(Ui.tv(a, txt, 12, col2, true));
            card.addView(cab);

            LinearLayout barra = Ui.barra(a, r[t][3],
                r[t][0] == 0 ? 0xFFCBD5E1 : kindColor(r[t][3]));
            LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Ui.dps(a, 6));
            blp.setMargins(0, Ui.dps(a, 9), 0, 0);
            barra.setLayoutParams(blp);
            card.addView(barra);

            TextView det = Ui.tv(a, r[t][1] + " dominadas · " + r[t][0] + " vistas de " + r[t][2],
                10, Ui.MUTED, false);
            det.setPadding(0, Ui.dps(a, 7), 0, 0);
            card.addView(det);

            col.addView(card);
        }

        col.addView(Ui.espacio(a, 10));
        col.addView(Ui.boton(a, "Repasar lo pendiente de hoy", Ui.ACCENT, 0xFFFFFFFF, () -> {
            a.volver();
            java.util.List<Banco.Pregunta> sel = pr.seleccionRepaso(20);
            a.navegar(new PantallaTest(a, sel, "Repaso de hoy", 0, false));
        }), Ui.full(a));
    }

    private int kindColor(int pct) {
        return pct >= 80 ? Ui.OK : (pct >= 60 ? Ui.WARN : Ui.KO);
    }

    private LinearLayout.LayoutParams peso(MainActivity a, int izq, int der) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p.setMargins(Ui.dps(a, izq), 0, Ui.dps(a, der), 0);
        return p;
    }

    private LinearLayout metrico(String valor, String etiqueta) {
        LinearLayout l = new LinearLayout(getContext());
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackground(Ui.redondeado(0x33FFFFFF, 12, getContext()));
        l.setPadding(0, Ui.dps(getContext(), 10), 0, Ui.dps(getContext(), 10));
        TextView v = Ui.tv(getContext(), valor, 17, 0xFFFFFFFF, true);
        v.setGravity(Gravity.CENTER);
        TextView e = Ui.tv(getContext(), etiqueta, 10, 0xB3FFFFFF, false);
        e.setGravity(Gravity.CENTER);
        l.addView(v);
        l.addView(e);
        return l;
    }

    private LinearLayout filaDato(String etiqueta, String valor) {
        LinearLayout f = Ui.fila(getContext());
        f.setPadding(0, Ui.dps(getContext(), 7), 0, Ui.dps(getContext(), 7));
        f.addView(Ui.tv(getContext(), etiqueta, 12, Ui.MUTED, false),
            new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        f.addView(Ui.tv(getContext(), valor, 13, Ui.TEXT, true));
        return f;
    }
}
