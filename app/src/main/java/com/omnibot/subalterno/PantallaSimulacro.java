package com.omnibot.subalterno;

import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Configuración del simulacro tipo examen oficial. */
final class PantallaSimulacro extends LinearLayout {

    PantallaSimulacro(MainActivity a) {
        super(a);
        setOrientation(VERTICAL);
        setBackgroundColor(Ui.BG);

        addView(a.barraSuperior("Simulacro", true), new LinearLayout.LayoutParams(
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

        col.addView(Ui.tv(a, "Simulacro completo", 22, Ui.TEXT, true));
        col.addView(Ui.espacio(a, 6));
        TextView s = Ui.tv(a,
            "30 preguntas de los 9 temas, cronometradas como en el examen oficial. "
            + "Cada pregunta concede 2 minutos; al agotarse el tiempo, la prueba se corrige sola.",
            12, Ui.MUTED, false);
        s.setLineSpacing(0, 1.35f);
        col.addView(s);
        col.addView(Ui.espacio(a, 20));

        // Info del formato
        LinearLayout info = Ui.tarjeta(a);
        info.addView(uiFila("Preguntas", "30"));
        info.addView(uiFila("Duración", "60 minutos"));
        info.addView(uiFila("Opciones", "A · B · C"));
        info.addView(uiFila("Corrección", "Al instante, con explicación"));
        col.addView(info);
        col.addView(Ui.espacio(a, 18));

        // Modos
        col.addView(Ui.tv(a, "Elige el modo", 14, Ui.TEXT, true));
        col.addView(Ui.espacio(a, 10));

        col.addView(modo("🕐", "Simulacro completo",
            "30 preguntas · 60 minutos · todos los temas", () -> {
            java.util.List<Banco.Pregunta> sel = a.progreso.seleccionAleatoria(0, 30);
            a.mostrar(new PantallaTest(a, sel, "Simulacro completo", 0, true));
        }));
        col.addView(Ui.espacio(a, 10));
        col.addView(modo("⚡", "Rápido", "15 preguntas · 20 minutos", () -> {
            java.util.List<Banco.Pregunta> sel = a.progreso.seleccionAleatoria(0, 15);
            a.mostrar(new PantallaTest(a, sel, "Simulacro rápido", 0, true));
        }));
        col.addView(Ui.espacio(a, 10));
        col.addView(modo("🎯", "Solo mis puntos débiles",
            "Preguntas que has fallado o no dominas", () -> {
            java.util.List<Banco.Pregunta> sel = a.progreso.seleccionRepaso(20);
            a.mostrar(new PantallaTest(a, sel, "Simulacro dirigido", 0, true));
        }));
    }

    private LinearLayout uiFila(String etiqueta, String valor) {
        LinearLayout f = Ui.fila(getContext());
        f.setPadding(0, Ui.dps(getContext(), 8), 0, Ui.dps(getContext(), 8));
        f.addView(Ui.tv(getContext(), etiqueta, 12, Ui.MUTED, false),
            new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        f.addView(Ui.tv(getContext(), valor, 13, Ui.TEXT, true));
        return f;
    }

    private LinearLayout modo(String emoji, String titulo, String sub, Runnable r) {
        LinearLayout l = Ui.fila(getContext());
        l.setBackground(Ui.ripple(Ui.redondeado(Ui.CARD, 16, getContext()), 0x14000000));
        l.setPadding(Ui.dps(getContext(), 14), Ui.dps(getContext(), 15),
            Ui.dps(getContext(), 14), Ui.dps(getContext(), 15));
        l.addView(Ui.circulo(getContext(), 0xFFFFF7ED, 40, emoji));
        LinearLayout mid = new LinearLayout(getContext());
        mid.setOrientation(LinearLayout.VERTICAL);
        mid.setPadding(Ui.dps(getContext(), 12), 0, 0, 0);
        mid.addView(Ui.tv(getContext(), titulo, 13, Ui.TEXT, true));
        TextView s = Ui.tv(getContext(), sub, 10, Ui.MUTED, false);
        s.setPadding(0, Ui.dps(getContext(), 3), 0, 0);
        mid.addView(s);
        l.addView(mid, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        l.addView(Ui.tv(getContext(), "›", 20, Ui.MUTED, false));
        l.setOnClickListener(v -> r.run());
        return l;
    }
}
