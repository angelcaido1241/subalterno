package com.omnibot.subalterno;

import android.app.Activity;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/** Utilidades visuales compartidas. */
public final class Ui {

    public static final int BG = 0xFFF6F8FC;
    public static final int CARD = 0xFFFFFFFF;
    public static final int PRIMARY = 0xFF1E3A8A;
    public static final int ACCENT = 0xFF2563EB;
    public static final int TEXT = 0xFF0F172A;
    public static final int MUTED = 0xFF64748B;
    public static final int OK = 0xFF16A34A;
    public static final int KO = 0xFFDC2626;
    public static final int WARN = 0xFFF59E0B;

    private Ui() {}

    // ---------- tipografía ----------
    public static TextView tv(Context c, String s, int size, int color, boolean bold) {
        TextView t = new TextView(c);
        t.setText(s);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT_BOLD);
        return t;
    }

    public static TextView titulo(Context c, String s) {
        TextView t = tv(c, s, 22, TEXT, true);
        t.setLetterSpacing(-0.01f);
        return t;
    }

    public static TextView subtitulo(Context c, String s) {
        return tv(c, s, 13, MUTED, false);
    }

    // ---------- formas ----------
    public static GradientDrawable redondeado(int color, float radioDp, Context c) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(color);
        g.setCornerRadius(dp(c, radioDp));
        return g;
    }

    public static GradientDrawable borde(int fill, int stroke, float radioDp, int anchoDp, Context c) {
        GradientDrawable g = new GradientDrawable();
        g.setColor(fill);
        g.setCornerRadius(dp(c, radioDp));
        g.setStroke((int) dp(c, anchoDp), stroke);
        return g;
    }

    public static GradientDrawable gradiente(int c1, int c2, float radioDp, Context c) {
        GradientDrawable g = new GradientDrawable(GradientDrawable.Orientation.TL_BR,
            new int[]{c1, c2});
        g.setCornerRadius(dp(c, radioDp));
        return g;
    }

    public static RippleDrawable ripple(GradientDrawable base, int rippleColor) {
        return new RippleDrawable(ColorStateList.valueOf(rippleColor), base, null);
    }

    public static float dp(Context c, float v) {
        return v * c.getResources().getDisplayMetrics().density;
    }

    public static int dps(Context c, float v) {
        return Math.round(dp(c, v));
    }

    // ---------- contenedores ----------
    public static LinearLayout tarjeta(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setBackground(redondeado(CARD, 18, c));
        l.setPadding(dps(c, 16), dps(c, 16), dps(c, 16), dps(c, 16));
        return l;
    }

    public static LinearLayout fila(Context c) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        return l;
    }

    public static LinearLayout.LayoutParams lp(int w, int h, Context c) {
        return new LinearLayout.LayoutParams(w, h);
    }

    public static LinearLayout.LayoutParams lpPeso(Context c, float peso) {
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        p.weight = peso;
        return p;
    }

    public static View espacio(Context c, int dp) {
        View v = new View(c);
        v.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dps(c, dp)));
        return v;
    }

    public static View separador(Context c) {
        View v = new View(c);
        v.setBackgroundColor(0xFFE9EEF6);
        v.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Math.max(1, dps(c, 1))));
        return v;
    }

    /** Scroll vertical con padding lateral estándar. */
    public static ScrollView scroll(Context c, View contenido) {
        ScrollView s = new ScrollView(c);
        s.setFillViewport(true);
        s.setClipToPadding(false);
        s.setPadding(dps(c, 18), 0, dps(c, 18), dps(c, 28));
        s.addView(contenido, new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return s;
    }

    /** Botón principal redondeado. */
    public static LinearLayout boton(Context c, String texto, int colorFondo, int colorTexto, Runnable accion) {
        LinearLayout b = new LinearLayout(c);
        b.setOrientation(LinearLayout.HORIZONTAL);
        b.setGravity(Gravity.CENTER);
        b.setBackground(ripple(redondeado(colorFondo, 14, c), 0x33FFFFFF));
        b.setPadding(dps(c, 16), dps(c, 15), dps(c, 16), dps(c, 15));
        TextView t = tv(c, texto, 15, colorTexto, true);
        t.setGravity(Gravity.CENTER);
        b.addView(t);
        b.setOnClickListener(v -> {
            if (accion != null) accion.run();
        });
        return b;
    }

    public static LinearLayout.LayoutParams full(Context c) {
        return new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    public static EditText campo(Context c, String hint) {
        EditText e = new EditText(c);
        e.setHint(hint);
        e.setTextSize(15);
        e.setTextColor(TEXT);
        e.setHintTextColor(MUTED);
        e.setBackground(borde(0xFFF1F5FB, 0xFFD7E1F0, 12, 1, c));
        e.setPadding(dps(c, 12), dps(c, 10), dps(c, 12), dps(c, 10));
        return e;
    }

    // ---------- barras de progreso ----------
    public static LinearLayout barra(Context c, int porcentaje, int color) {
        LinearLayout fondo = new LinearLayout(c);
        fondo.setBackground(redondeado(0xFFE6ECF6, 6, c));
        fondo.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, dps(c, 8)));
        View relleno = new View(c);
        int p = Math.max(0, Math.min(100, porcentaje));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dps(c, 8));
        lp.weight = Math.max(0.0001f, p / 100f);
        relleno.setLayoutParams(lp);
        relleno.setBackground(redondeado(color, 6, c));
        View hueco = new View(c);
        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, dps(c, 8));
        lp2.weight = Math.max(0.0001f, (100 - p) / 100f);
        hueco.setLayoutParams(lp2);
        fondo.addView(relleno);
        if (p < 100) fondo.addView(hueco);
        return fondo;
    }

    // ---------- píldoras ----------
    public static TextView pildora(Context c, String texto, int colorFondo, int colorTexto) {
        TextView t = tv(c, texto, 11, colorTexto, true);
        t.setBackground(redondeado(colorFondo, 20, c));
        t.setPadding(dps(c, 9), dps(c, 4), dps(c, 9), dps(c, 4));
        return t;
    }

    /** Encabezado de pantalla con título grande y subtítulo. */
    public static LinearLayout encabezado(Context c, String titulo, String sub) {
        LinearLayout l = new LinearLayout(c);
        l.setOrientation(LinearLayout.VERTICAL);
        l.setPadding(dps(c, 18), dps(c, 26), dps(c, 18), dps(c, 10));
        l.addView(titulo(c, titulo));
        if (sub != null && !sub.isEmpty()) {
            TextView s = subtitulo(c, sub);
            s.setPadding(0, dps(c, 4), 0, 0);
            l.addView(s);
        }
        return l;
    }

    public static View circulo(Context c, int color, int tam, String emoji) {
        FrameLayout f = new FrameLayout(c);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(dps(c, tam), dps(c, tam));
        f.setLayoutParams(lp);
        f.setBackground(redondeado(color, tam / 2f, c));
        TextView t = tv(c, emoji, Math.round(tam * 0.42f), 0xFFFFFFFF, false);
        t.setGravity(Gravity.CENTER);
        FrameLayout.LayoutParams flp = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        f.addView(t, flp);
        return f;
    }
}
