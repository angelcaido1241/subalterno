package com.omnibot.subalterno;

import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

/** Motor de test: muestra preguntas, corrige al instante y guarda progreso. */
final class PantallaTest extends LinearLayout {

    private final MainActivity act;
    private final List<Banco.Pregunta> preguntas;
    private final String titulo;
    private final int tema;
    private final boolean cronometrado;

    private int indice = 0;
    private int aciertos = 0;
    private int fallos = 0;
    private boolean respondida = false;
    private long inicioMs = System.currentTimeMillis();

    private CountDownTimer crono;
    private TextView cronoVista;

    private LinearLayout cuerpo;
    private LinearLayout zonaOpciones;
    private LinearLayout zonaExplicacion;
    private LinearLayout botonera;
    private TextView marcador;

    PantallaTest(MainActivity a, List<Banco.Pregunta> preguntas, String titulo,
                 int tema, boolean cronometrado) {
        super(a);
        this.act = a;
        this.preguntas = preguntas;
        this.titulo = titulo;
        this.tema = tema;
        this.cronometrado = cronometrado;

        setOrientation(VERTICAL);
        setBackgroundColor(Ui.BG);

        addView(construirCabecera(), new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ScrollView sc = new ScrollView(a);
        sc.setClipToPadding(false);
        sc.setFillViewport(true);
        cuerpo = new LinearLayout(a);
        cuerpo.setOrientation(VERTICAL);
        cuerpo.setPadding(Ui.dps(a, 18), Ui.dps(a, 16), Ui.dps(a, 18), Ui.dps(a, 24));
        sc.addView(cuerpo, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(sc, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, 0, 1f));

        if (cronometrado) iniciarCrono();
        pintarPregunta();
    }

    // ---------- cabecera con progreso y cronómetro ----------
    private LinearLayout cab;

    private LinearLayout construirCabecera() {
        cab = new LinearLayout(act);
        cab.setOrientation(VERTICAL);
        cab.setBackgroundColor(0xFFFFFFFF);
        cab.setPadding(Ui.dps(act, 14), Ui.dps(act, 12), Ui.dps(act, 14), Ui.dps(act, 10));

        LinearLayout fila1 = Ui.fila(act);
        TextView atras = Ui.tv(act, "✕", 18, Ui.MUTED, false);
        atras.setGravity(Gravity.CENTER);
        atras.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(act, 32), Ui.dps(act, 32)));
        atras.setOnClickListener(v -> {
            if (crono != null) crono.cancel();
            act.volver();
        });
        fila1.addView(atras);

        String t = titulo.length() > 26 ? titulo.substring(0, 26) + "…" : titulo;
        TextView tit = Ui.tv(act, t, 12, Ui.TEXT, true);
        fila1.addView(tit, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        marcador = Ui.tv(act, "", 11, Ui.MUTED, true);
        fila1.addView(marcador);
        cab.addView(fila1);

        cab.addView(Ui.espacio(act, 8));
        cab.addView(Ui.barra(act, 0, Ui.ACCENT), new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Ui.dps(act, 5)));

        if (cronometrado) {
            cronoVista = Ui.tv(act, "⏱ 00:00", 12, Ui.ACCENT, true);
            cronoVista.setPadding(0, Ui.dps(act, 7), 0, 0);
            cab.addView(cronoVista);
        }
        return cab;
    }

    private void iniciarCrono() {
        int minutos = Math.max(5, preguntas.size());
        long total = minutos * 60_000L;
        crono = new CountDownTimer(total, 1000) {
            @Override public void onTick(long ms) {
                if (cronoVista == null) return;
                long s = ms / 1000;
                cronoVista.setText(String.format("⏱ %02d:%02d", s / 60, s % 60));
                if (s <= 60) cronoVista.setTextColor(Ui.KO);
            }
            @Override public void onFinish() {
                if (cronoVista != null) cronoVista.setText("⏱ Tiempo agotado");
                finalizar();
            }
        }.start();
    }

    // ---------- pintado de la pregunta ----------
    private void pintarPregunta() {
        if (indice >= preguntas.size()) { finalizar(); return; }
        respondida = false;
        cuerpo.removeAllViews();

        if (marcador != null) {
            marcador.setText((indice + 1) + "/" + preguntas.size()
                + "  ·  ✓" + aciertos + " ✗" + fallos);
        }

        Banco.Pregunta q = preguntas.get(indice);

        // indicador de tema
        String etiqueta = q.tema >= 1 && q.tema <= 9
            ? Temas.NOMBRES[q.tema].replace("Tema " + q.tema + " · ", "")
            : "General";
        TextView pill = Ui.pildora(act, etiqueta, 0xFFEFF6FF, Ui.ACCENT);
        LinearLayout filaPill = Ui.fila(act);
        filaPill.addView(pill);
        filaPill.addView(new View(act), new LinearLayout.LayoutParams(0, 1, 1f));
        cuerpo.addView(filaPill);
        cuerpo.addView(Ui.espacio(act, 12));

        // enunciado
        TextView en = Ui.tv(act, q.enunciado, 17, Ui.TEXT, true);
        en.setLineSpacing(0, 1.35f);
        cuerpo.addView(en);
        cuerpo.addView(Ui.espacio(act, 18));

        // opciones
        zonaOpciones = new LinearLayout(act);
        zonaOpciones.setOrientation(VERTICAL);
        for (int i = 0; i < q.opciones.length; i++) {
            final int idx = i;
            zonaOpciones.addView(opcion(q, idx, i, () -> responder(q, idx)));
        }
        cuerpo.addView(zonaOpciones);

        // zona de explicación (se rellena al responder)
        zonaExplicacion = new LinearLayout(act);
        zonaExplicacion.setOrientation(VERTICAL);
        cuerpo.addView(zonaExplicacion);

        // botonera
        botonera = new LinearLayout(act);
        botonera.setOrientation(VERTICAL);
        botonera.setPadding(0, Ui.dps(act, 16), 0, 0);
        cuerpo.addView(botonera);

        if (!cronometrado) {
            TextView salir = Ui.tv(act, "Terminar test", 12, Ui.MUTED, true);
            salir.setGravity(Gravity.CENTER);
            salir.setPadding(0, Ui.dps(act, 14), 0, 0);
            salir.setOnClickListener(v -> finalizar());
            cuerpo.addView(salir);
        }

        // actualizar barra de progreso
        if (cab.getChildCount() > 2) {
            View barra = cab.getChildAt(2);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) barra.getLayoutParams();
            int pct = Math.round(indice * 100f / Math.max(1, preguntas.size()));
            cab.removeViewAt(2);
            cab.addView(Ui.barra(act, pct, Ui.ACCENT), 2, lp);
        }
    }

    private LinearLayout opcion(Banco.Pregunta q, int idx, int letra, Runnable accion) {
        LinearLayout l = new LinearLayout(act);
        l.setOrientation(HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        l.setBackground(Ui.ripple(Ui.borde(0xFFFFFFFF, 0xFFE2E8F2, 14, 1, act), 0x10000000));
        l.setPadding(Ui.dps(act, 14), Ui.dps(act, 14), Ui.dps(act, 14), Ui.dps(act, 14));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, Ui.dps(act, 9));
        l.setLayoutParams(lp);

        String[] letras = {"A", "B", "C", "D"};
        TextView cir = Ui.tv(act, letras[letra], 12, Ui.ACCENT, true);
        cir.setGravity(Gravity.CENTER);
        cir.setBackground(Ui.redondeado(0xFFEFF6FF, 9, act));
        cir.setLayoutParams(new LinearLayout.LayoutParams(Ui.dps(act, 28), Ui.dps(act, 28)));
        l.addView(cir);

        TextView txt = Ui.tv(act, q.opciones[idx], 14, Ui.TEXT, false);
        txt.setPadding(Ui.dps(act, 12), 0, 0, 0);
        txt.setLineSpacing(0, 1.3f);
        l.addView(txt, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        final int marcadorLetra = letra;
        l.setOnClickListener(v -> {
            if (!respondida) accion.run();
        });
        l.setTag(new Object[]{letra, cir, txt});
        return l;
    }

    private void responder(Banco.Pregunta q, int elegida) {
        respondida = true;
        boolean acierto = elegida == q.correcta;
        if (acierto) aciertos++; else fallos++;
        act.progreso.registrar(q, acierto);

        // colorear opciones
        for (int i = 0; i < zonaOpciones.getChildCount(); i++) {
            LinearLayout fila = (LinearLayout) zonaOpciones.getChildAt(i);
            Object[] meta = (Object[]) fila.getTag();
            int letra = (Integer) meta[0];
            TextView cir = (TextView) meta[1];
            TextView txt = (TextView) meta[2];

            if (letra == q.correcta) {
                fila.setBackground(Ui.borde(0xFFF0FDF4, Ui.OK, 14, 2, act));
                cir.setBackground(Ui.redondeado(Ui.OK, 9, act));
                cir.setTextColor(0xFFFFFFFF);
                txt.setTextColor(0xFF14532D);
                txt.setTypeface(Typeface.DEFAULT_BOLD);
            } else if (letra == elegida) {
                fila.setBackground(Ui.borde(0xFFFEF2F2, Ui.KO, 14, 2, act));
                cir.setBackground(Ui.redondeado(Ui.KO, 9, act));
                cir.setTextColor(0xFFFFFFFF);
                txt.setTextColor(0xFF7F1D1D);
            } else {
                fila.setAlpha(0.5f);
            }
        }

        // explicación
        zonaExplicacion.removeAllViews();
        zonaExplicacion.addView(Ui.espacio(act, 14));

        LinearLayout caja = new LinearLayout(act);
        caja.setOrientation(VERTICAL);
        caja.setBackground(Ui.redondeado(acierto ? 0xFFF0FDF4 : 0xFFFEF2F2, 14, act));
        caja.setPadding(Ui.dps(act, 14), Ui.dps(act, 12), Ui.dps(act, 14), Ui.dps(act, 12));

        TextView titulo = Ui.tv(act, acierto ? "✓ Correcto" : "✗ Incorrecto",
            13, acierto ? 0xFF15803D : 0xFFB91C1C, true);
        caja.addView(titulo);

        if (!acierto) {
            String[] letras = {"A", "B", "C", "D"};
            TextView corr = Ui.tv(act,
                "La correcta es la " + letras[q.correcta] + ": " + q.opciones[q.correcta],
                12, 0xFF7F1D1D, true);
            corr.setPadding(0, Ui.dps(act, 6), 0, 0);
            corr.setLineSpacing(0, 1.25f);
            caja.addView(corr);
        }

        TextView exp = Ui.tv(act, q.explicacion, 12, 0xFF374151, false);
        exp.setPadding(0, Ui.dps(act, 8), 0, 0);
        exp.setLineSpacing(0, 1.35f);
        caja.addView(exp);

        zonaExplicacion.addView(caja);

        // botón siguiente
        botonera.removeAllViews();
        boolean ultima = indice == preguntas.size() - 1;
        String texto = ultima ? "Ver resultados" : "Siguiente pregunta →";
        botonera.addView(Ui.boton(act, texto, Ui.ACCENT, 0xFFFFFFFF, () -> {
            indice++;
            pintarPregunta();
        }), Ui.full(act));

        if (marcador != null) {
            marcador.setText((indice + 1) + "/" + preguntas.size()
                + "  ·  ✓" + aciertos + " ✗" + fallos);
        }
    }

    private void finalizar() {
        if (crono != null) { crono.cancel(); crono = null; }
        int minutos = (int) Math.max(1, (System.currentTimeMillis() - inicioMs) / 60000);
        act.progreso.sumarMinutos(minutos);
        respondidas = aciertos + fallos;
        if (cronometrado) act.progreso.sumarSimulacro();
        else act.progreso.sumarTest();
        act.mostrar(new PantallaResultado(act, aciertos, fallos, preguntas.size() - respondidas,
            titulo, tema, minutos, cronometrado));
    }

    private int respondidas = 0;
}
