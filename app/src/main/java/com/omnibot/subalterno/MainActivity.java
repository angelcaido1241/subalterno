package com.omnibot.subalterno;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayDeque;
import java.util.Deque;

/** Actividad única: gestiona la navegación entre pantallas y la barra inferior. */
public class MainActivity extends Activity {

    public static final int TAB_INICIO = 0;
    public static final int TAB_TEMAS = 1;
    public static final int TAB_PROGRESO = 2;
    public static final int TAB_AJUSTES = 3;

    private FrameLayout contenedor;
    private LinearLayout barra;
    private final Deque<Runnable[]> pila = new ArrayDeque<>();
    public Progreso progreso;

    @Override
    protected void onCreate(Bundle b) {
        super.onCreate(b);
        progreso = new Progreso(this);

        FrameLayout raiz = new FrameLayout(this);
        raiz.setBackgroundColor(Ui.BG);

        contenedor = new FrameLayout(this);
        FrameLayout.LayoutParams lpCnt = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lpCnt.bottomMargin = Ui.dps(this, 62);
        raiz.addView(contenedor, lpCnt);

        barra = new LinearLayout(this);
        barra.setOrientation(LinearLayout.HORIZONTAL);
        barra.setBackgroundColor(0xFFFFFFFF);
        barra.setElevation(Ui.dp(this, 10));
        FrameLayout.LayoutParams lpBar = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, Ui.dps(this, 62));
        lpBar.gravity = Gravity.BOTTOM;
        raiz.addView(barra, lpBar);

        setContentView(raiz);
        irTab(TAB_INICIO);
    }

    // ================= NAVEGACIÓN =================

    public void irTab(int tab) {
        pila.clear();
        pintarBarra(tab);
        switch (tab) {
            case TAB_TEMAS: mostrar(new PantallaTemas(this)); break;
            case TAB_PROGRESO: mostrar(new PantallaProgreso(this)); break;
            case TAB_AJUSTES: mostrar(new PantallaAjustes(this)); break;
            default: mostrar(new PantallaInicio(this));
        }
    }

    /** Muestra una pantalla sin barra inferior (tests, resultados...). */
    public void mostrar(View vista) {
        contenedor.removeAllViews();
        contenedor.addView(vista, new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    /** Empuja una pantalla para poder volver atrás. */
    public void navegar(View vista) {
        pila.push(new Runnable[]{() -> { }});
        mostrar(vista);
        barra.setVisibility(View.GONE);
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) contenedor.getLayoutParams();
        lp.bottomMargin = 0;
        contenedor.setLayoutParams(lp);
    }

    public void volver() {
        if (!pila.isEmpty()) pila.pop();
        if (pila.isEmpty()) {
            barra.setVisibility(View.VISIBLE);
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) contenedor.getLayoutParams();
            lp.bottomMargin = Ui.dps(this, 62);
            contenedor.setLayoutParams(lp);
            irTab(TAB_INICIO);
        }
    }

    @Override
    public void onBackPressed() {
        if (!pila.isEmpty()) { pila.pop(); volver(); }
        else if (contenedor.getChildCount() > 0
                && !(contenedor.getChildAt(0) instanceof ScrollView)) {
            irTab(TAB_INICIO);
        } else {
            super.onBackPressed();
        }
    }

    // ================= BARRA INFERIOR =================

    private void pintarBarra(int activo) {
        barra.removeAllViews();
        barra.setVisibility(View.VISIBLE);
        String[] iconos = {"🏠", "📚", "📊", "⚙️"};
        String[] textos = {"Inicio", "Temas", "Progreso", "Ajustes"};
        for (int i = 0; i < 4; i++) {
            final int idx = i;
            LinearLayout item = new LinearLayout(this);
            item.setOrientation(LinearLayout.VERTICAL);
            item.setGravity(Gravity.CENTER);
            item.setLayoutParams(new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.MATCH_PARENT, 1f));
            TextView ic = Ui.tv(this, iconos[i], 18, idx == activo ? Ui.ACCENT : Ui.MUTED, false);
            ic.setGravity(Gravity.CENTER);
            TextView tx = Ui.tv(this, textos[i], 10,
                idx == activo ? Ui.ACCENT : Ui.MUTED, idx == activo);
            tx.setGravity(Gravity.CENTER);
            tx.setPadding(0, Ui.dps(this, 2), 0, 0);
            item.addView(ic);
            item.addView(tx);
            item.setOnClickListener(v -> irTab(idx));
            barra.addView(item);
        }
    }

    /** Barra superior reutilizable con botón atrás y título. */
    public LinearLayout barraSuperior(String titulo, boolean conAtras) {
        LinearLayout l = new LinearLayout(this);
        l.setOrientation(LinearLayout.HORIZONTAL);
        l.setGravity(Gravity.CENTER_VERTICAL);
        l.setBackgroundColor(0xFFFFFFFF);
        l.setPadding(Ui.dps(this, 10), Ui.dps(this, 12), Ui.dps(this, 14), Ui.dps(this, 12));
        if (conAtras) {
            TextView atras = Ui.tv(this, "←", 24, Ui.TEXT, false);
            atras.setGravity(Gravity.CENTER);
            atras.setLayoutParams(new LinearLayout.LayoutParams(
                Ui.dps(this, 40), Ui.dps(this, 40)));
            atras.setOnClickListener(v -> volver());
            l.addView(atras);
        }
        TextView t = Ui.tv(this, titulo, 16, Ui.TEXT, true);
        t.setPadding(Ui.dps(this, conAtras ? 4 : 8), 0, 0, 0);
        l.addView(t, new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        return l;
    }
}
