package com.omnibot.subalterno;

import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.ViewGroup;

/** Listado de los 9 temas con su progreso. */
final class PantallaTemas extends ScrollView {

    private final MainActivity act;

    PantallaTemas(MainActivity a) {
        super(a);
        this.act = a;
        setBackgroundColor(Ui.BG);
        setClipToPadding(false);

        LinearLayout col = new LinearLayout(a);
        col.setOrientation(LinearLayout.VERTICAL);
        col.setPadding(Ui.dps(a, 18), Ui.dps(a, 26), Ui.dps(a, 18), Ui.dps(a, 30));
        addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        col.addView(Ui.titulo(a, "Temario"));
        col.addView(Ui.subtitulo(a, "9 temas · " + Banco.todas().size() + " preguntas oficiales"));
        col.addView(Ui.espacio(a, 16));

        int[][] r = act.progreso.porTema();

        for (int t = 1; t <= 9; t++) {
            final int tema = t;
            LinearLayout card = Ui.tarjeta(a);
            LinearLayout.LayoutParams clp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            clp.setMargins(0, 0, 0, Ui.dps(a, 10));
            card.setLayoutParams(clp);

            LinearLayout cab = Ui.fila(a);
            TextView num = Ui.tv(a, String.valueOf(t), 14, 0xFFFFFFFF, true);
            num.setGravity(android.view.Gravity.CENTER);
            num.setBackground(Ui.redondeado(t % 2 == 0 ? Ui.ACCENT : Ui.PRIMARY, 11, a));
            num.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(a, 32), Ui.dps(a, 32)));
            cab.addView(num);

            LinearLayout mid = new LinearLayout(a);
            mid.setOrientation(LinearLayout.VERTICAL);
            mid.setPadding(Ui.dps(a, 12), 0, 0, 0);
            String corto = Temas.NOMBRES[t].replace("Tema " + t + " · ", "");
            mid.addView(Ui.tv(a, corto, 14, Ui.TEXT, true));
            TextView info = Ui.tv(a, r[t][1] + " dominadas de " + r[t][2],
                11, Ui.MUTED, false);
            info.setPadding(0, Ui.dps(a, 3), 0, 0);
            mid.addView(info);
            cab.addView(mid, new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

            int pct = r[t][3];
            TextView pctTv = Ui.tv(a, r[t][0] == 0 ? "—" : pct + "%", 13,
                r[t][0] == 0 ? Ui.MUTED : (pct >= 80 ? Ui.OK : (pct >= 60 ? Ui.WARN : Ui.KO)), true);
            cab.addView(pctTv);
            card.addView(cab);

            card.addView(Ui.espacio(a, 12));
            int avance = r[t][2] == 0 ? 0 : Math.round(r[t][1] * 100f / r[t][2]);
            card.addView(Ui.barra(a, avance, Ui.ACCENT));

            card.addView(Ui.espacio(a, 12));
            TextView resumen = Ui.tv(a, Temas.RESUMEN[t], 11, Ui.MUTED, false);
            resumen.setLineSpacing(0, 1.25f);
            card.addView(resumen);

            card.setOnClickListener(v -> act.navegar(new PantallaTema(act, tema)));
            col.addView(card);
        }
    }
}
