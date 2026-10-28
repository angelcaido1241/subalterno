package com.omnibot.subalterno;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Pantalla de inicio: cuenta atrás, racha y accesos rápidos. */
final class PantallaInicio extends ScrollView {

    private final MainActivity act;
    private final Progreso pr;

    PantallaInicio(MainActivity a) {
        super(a);
        this.act = a;
        this.pr = a.progreso;
        setBackgroundColor(Ui.BG);
        setClipToPadding(false);
        setPadding(0, 0, 0, Ui.dps(a, 24));

        LinearLayout col = new LinearLayout(a);
        col.setOrientation(LinearLayout.VERTICAL);
        addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        col.addView(hero());
        col.addView(bloqueAcciones());
        col.addView(bloqueRecomendado());
        col.addView(bloqueTemasRapidos());

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(Ui.dps(a, 18), Ui.dps(a, 14), Ui.dps(a, 18), 0);
        col.setLayoutParams(lp);
    }

    // ---------- tarjeta superior ----------
    private View hero() {
        long dias = Progreso.diasHasta(pr.fechaExamen());

        LinearLayout hero = new LinearLayout(act);
        hero.setOrientation(LinearLayout.VERTICAL);
        hero.setPadding(Ui.dps(act, 20), Ui.dps(act, 20), Ui.dps(act, 20), Ui.dps(act, 20));
        hero.setBackground(Ui.gradiente(0xFF1E3A8A, 0xFF2563EB, 20, act));
        hero.setElevation(Ui.dp(act, 4));

        LinearLayout filaTop = Ui.fila(act);
        TextView marca = Ui.tv(act, "SUBALTERNO · 81/26", 11, 0xCCFFFFFF, true);
        marca.setLetterSpacing(0.12f);
        filaTop.addView(marca, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView rachaTv = Ui.tv(act, "🔥 " + pr.racha() + " días", 12, 0xFFFFFFFF, true);
        rachaTv.setBackground(Ui.redondeado(0x33FFFFFF, 20, act));
        rachaTv.setPadding(Ui.dps(act, 10), Ui.dps(act, 5), Ui.dps(act, 10), Ui.dps(act, 5));
        filaTop.addView(rachaTv);
        hero.addView(filaTop);

        hero.addView(Ui.espacio(act, 16));

        TextView diasTv = Ui.tv(act, String.valueOf(dias), 46, 0xFFFFFFFF, true);
        diasTv.setIncludeFontPadding(false);
        hero.addView(diasTv);
        TextView etiqueta = Ui.tv(act, dias == 1 ? "día para el examen" : "días para el examen",
            13, 0xB3FFFFFF, false);
        hero.addView(etiqueta);

        hero.addView(Ui.espacio(act, 14));

        LinearLayout barraCont = new LinearLayout(act);
        barraCont.setOrientation(LinearLayout.VERTICAL);
        int vistas = pr.totalVistas();
        int total = Banco.todas().size();
        int pct = total == 0 ? 0 : Math.round(vistas * 100f / total);
        LinearLayout barra = Ui.barra(act, pct, 0xFF60A5FA);
        // sobre fondo oscuro, aclaramos el fondo de la barra
        barra.setBackground(Ui.redondeado(0x33FFFFFF, 6, act));
        barraCont.addView(barra);
        LinearLayout filaInfo = Ui.fila(act);
        filaInfo.setPadding(0, Ui.dps(act, 8), 0, 0);
        filaInfo.addView(Ui.tv(act, vistas + " de " + total + " preguntas vistas", 11,
            0xCCFFFFFF, false), new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        filaInfo.addView(Ui.tv(act, pct + "%", 11, 0xFFFFFFFF, true));
        barraCont.addView(filaInfo);
        hero.addView(barraCont);

        return hero;
    }

    // ---------- accesos rápidos ----------
    private View bloqueAcciones() {
        LinearLayout cont = new LinearLayout(act);
        cont.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, Ui.dps(act, 18), 0, 0);
        cont.setLayoutParams(lp);

        LinearLayout f1 = Ui.fila(act);
        f1.addView(accion("🎯", "Test por tema", "Preguntas de un tema concreto",
            0xFFEFF6FF, 0xFF2563EB, () -> act.irTab(MainActivity.TAB_TEMAS)),
            pesoConMargen(0, 10));
        f1.addView(accion("⏱️", "Simulacro", "Cronometrado, como el examen",
            0xFFFFF7ED, 0xFFEA580C, () -> act.navegar(new PantallaSimulacro(act))),
            pesoConMargen(10, 0));
        cont.addView(f1);

        cont.addView(Ui.espacio(act, 10));

        LinearLayout f2 = Ui.fila(act);
        f2.addView(accion("🔁", "Repaso de hoy", pr.pendientesHoy() + " pendientes",
            0xFFF0FDF4, 0xFF16A34A, () -> {
                java.util.List<Banco.Pregunta> sel = pr.seleccionRepaso(20);
                act.navegar(new PantallaTest(act, sel, "Repaso de hoy", -1, false));
            }), pesoConMargen(0, 10));
        f2.addView(accion("❗", "Mis fallos", pr.preguntasFalladas().size() + " por dominar",
            0xFFFEF2F2, 0xFFDC2626, () -> {
                java.util.List<Banco.Pregunta> fallos = pr.preguntasFalladas();
                if (fallos.isEmpty()) {
                    act.navegar(new PantallaTest(act, pr.seleccionAleatoria(0, 20),
                        "Sin fallos pendientes · preguntas generales", 0, false));
                } else {
                    act.navegar(new PantallaTest(act, fallos, "Mis fallos", 0, false));
                }
            }), pesoConMargen(10, 0));
        cont.addView(f2);

        return cont;
    }

    private LinearLayout.LayoutParams pesoConMargen(int izq, int der) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        p.setMargins(Ui.dps(act, izq), 0, Ui.dps(act, der), 0);
        return p;
    }

    private View accion(String emoji, String titulo, String sub, int fondo, int color, Runnable r) {
        LinearLayout l = new LinearLayout(act);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackground(Ui.ripple(Ui.redondeado(Ui.CARD, 16, act), 0x14000000));
        l.setPadding(Ui.dps(act, 14), Ui.dps(act, 14), Ui.dps(act, 14), Ui.dps(act, 14));
        l.setMinimumHeight(Ui.dps(act, 104));

        TextView ic = Ui.tv(act, emoji, 18, color, false);
        ic.setGravity(Gravity.CENTER);
        ic.setBackground(Ui.redondeado(fondo, 10, act));
        ic.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(act, 34), Ui.dps(act, 34)));
        l.addView(ic);

        l.addView(Ui.espacio(act, 10));
        l.addView(Ui.tv(act, titulo, 13, Ui.TEXT, true));
        TextView s = Ui.tv(act, sub, 10, Ui.MUTED, false);
        s.setPadding(0, Ui.dps(act, 2), 0, 0);
        l.addView(s);
        l.setOnClickListener(v -> r.run());
        return l;
    }

    // ---------- sesión recomendada ----------
    private View bloqueRecomendado() {
        LinearLayout card = Ui.tarjeta(act);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, Ui.dps(act, 14), 0, 0);
        card.setLayoutParams(lp);

        LinearLayout fila = Ui.fila(act);
        fila.addView(Ui.tv(act, "Sesión recomendada", 14, Ui.TEXT, true),
            new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView badge = Ui.pildora(act, "20 preguntas", 0xFFEFF6FF, Ui.ACCENT);
        fila.addView(badge);
        card.addView(fila);

        card.addView(Ui.espacio(act, 10));

        int[] datos = temaMasDebil();
        int tema = datos[0];
        String nombre = tema == 0 ? "Repaso general de los 9 temas" : Temas.NOMBRES[tema];
        TextView sub = Ui.tv(act, nombre, 12, Ui.MUTED, false);
        card.addView(sub);

        card.addView(Ui.espacio(act, 12));

        LinearLayout b = Ui.boton(act, "Empezar ahora →", Ui.ACCENT, 0xFFFFFFFF, () -> {
            java.util.List<Banco.Pregunta> sel = pr.seleccionAleatoria(tema, 20);
            act.navegar(new PantallaTest(act, sel,
                tema == 0 ? "Repaso general" : Temas.NOMBRES[tema], tema, false));
        });
        card.addView(b, Ui.full(act));
        return card;
    }

    /** Devuelve el tema con menor porcentaje de acierto (0 = general). */
    private int[] temaMasDebil() {
        int[][] r = pr.porTema();
        int peor = 0, peorPct = 101;
        for (int t = 1; t <= 9; t++) {
            if (r[t][0] == 0) continue; // sin datos
            if (r[t][3] < peorPct) { peorPct = r[t][3]; peor = t; }
        }
        if (peor == 0) return new int[]{0, 0};
        return new int[]{peor, peorPct};
    }

    // ---------- temas rápidos ----------
    private View bloqueTemasRapidos() {
        LinearLayout cont = new LinearLayout(act);
        cont.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, Ui.dps(act, 18), 0, 0);
        cont.setLayoutParams(lp);

        LinearLayout fila = Ui.fila(act);
        fila.addView(Ui.tv(act, "Continúa por donde ibas", 14, Ui.TEXT, true),
            new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        TextView ver = Ui.tv(act, "Ver todos", 12, Ui.ACCENT, true);
        ver.setOnClickListener(v -> act.irTab(MainActivity.TAB_TEMAS));
        fila.addView(ver);
        cont.addView(fila);

        cont.addView(Ui.espacio(act, 10));

        int[][] r = pr.porTema();
        for (int t = 1; t <= 9; t++) {
            final int tema = t;
            LinearLayout item = Ui.fila(act);
            item.setBackground(Ui.ripple(Ui.redondeado(Ui.CARD, 14, act), 0x14000000));
            item.setPadding(Ui.dps(act, 12), Ui.dps(act, 12), Ui.dps(act, 12), Ui.dps(act, 12));
            LinearLayout.LayoutParams ilp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ilp.setMargins(0, 0, 0, Ui.dps(act, 8));
            item.setLayoutParams(ilp);

            TextView num = Ui.tv(act, String.valueOf(t), 13, 0xFFFFFFFF, true);
            num.setGravity(Gravity.CENTER);
            num.setBackground(Ui.redondeado(Ui.ACCENT, 10, act));
            num.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(act, 30), Ui.dps(act, 30)));
            item.addView(num);

            LinearLayout mid = new LinearLayout(act);
            mid.setOrientation(LinearLayout.VERTICAL);
            mid.setPadding(Ui.dps(act, 12), 0, Ui.dps(act, 12), 0);
            String corto = Temas.NOMBRES[t].replace("Tema " + t + " · ", "");
            mid.addView(Ui.tv(act, corto, 12, Ui.TEXT, true));
            int pct = r[t][3];
            LinearLayout barra = Ui.barra(act, pct,
                pct >= 80 ? Ui.OK : (pct >= 60 ? Ui.WARN : Ui.KO));
            LinearLayout.LayoutParams blp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, Ui.dps(act, 6));
            blp.setMargins(0, Ui.dps(act, 7), 0, 0);
            barra.setLayoutParams(blp);
            mid.addView(barra);
            item.addView(mid, new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            TextView pctTv = Ui.tv(act, r[t][0] == 0 ? "—" : pct + "%", 12,
                r[t][0] == 0 ? Ui.MUTED : (pct >= 80 ? Ui.OK : (pct >= 60 ? Ui.WARN : Ui.KO)), true);
            item.addView(pctTv);

            item.setOnClickListener(v -> act.navegar(
                new PantallaTema(act, tema)));
            cont.addView(item);
        }
        return cont;
    }
}
