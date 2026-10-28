package com.omnibot.subalterno;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.util.Calendar;

/** Ajustes: recordatorio diario, fecha de examen y opciones generales. */
final class PantallaAjustes extends LinearLayout {

    private final MainActivity act;
    private final Progreso pr;

    PantallaAjustes(MainActivity a) {
        super(a);
        this.act = a;
        this.pr = a.progreso;
        setOrientation(VERTICAL);
        setBackgroundColor(Ui.BG);

        ScrollView sc = new ScrollView(a);
        sc.setClipToPadding(false);
        LinearLayout col = new LinearLayout(a);
        col.setOrientation(VERTICAL);
        col.setPadding(Ui.dps(a, 18), Ui.dps(a, 26), Ui.dps(a, 18), Ui.dps(a, 30));
        sc.addView(col, new ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(sc, new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        col.addView(Ui.titulo(a, "Ajustes"));
        col.addView(Ui.subtitulo(a, "Personaliza tu preparación"));
        col.addView(Ui.espacio(a, 18));

        // ---------- Recordatorio diario ----------
        LinearLayout rec = Ui.tarjeta(a);
        rec.addView(Ui.tv(a, "⏰ Recordatorio diario", 14, Ui.TEXT, true));
        rec.addView(Ui.espacio(a, 6));
        rec.addView(Ui.tv(a, "Una notificación al día para que no pierdas la racha.",
            11, Ui.MUTED, false));
        rec.addView(Ui.espacio(a, 12));

        TimePicker tp = new TimePicker(a);
        tp.setIs24HourView(true);
        int hora = a.getSharedPreferences("conf", Context.MODE_PRIVATE).getInt("hora", 19);
        int min = a.getSharedPreferences("conf", Context.MODE_PRIVATE).getInt("min", 0);
        tp.setHour(hora);
        tp.setMinute(min);
        tp.setLayoutParams(new LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, Ui.dps(a, 110)));
        rec.addView(tp);

        rec.addView(Ui.espacio(a, 10));
        LinearLayout filaBtn = Ui.fila(a);
        LinearLayout b1 = Ui.boton(a, "Activar recordatorio", Ui.ACCENT, 0xFFFFFFFF, () -> {
            a.getSharedPreferences("conf", Context.MODE_PRIVATE).edit()
                .putInt("hora", tp.getHour()).putInt("min", tp.getMinute())
                .putBoolean("recordatorio", true).apply();
            programar(a, tp.getHour(), tp.getMinute());
            android.widget.Toast.makeText(a,
                String.format("Recordatorio programado a las %02d:%02d", tp.getHour(), tp.getMinute()),
                android.widget.Toast.LENGTH_SHORT).show();
        });
        b1.setLayoutParams(new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
        filaBtn.addView(b1);

        LinearLayout b2 = Ui.boton(a, "Cancelar", 0xFFF1F5F9, Ui.MUTED, () -> {
            cancelar(a);
            a.getSharedPreferences("conf", Context.MODE_PRIVATE).edit()
                .putBoolean("recordatorio", false).apply();
            android.widget.Toast.makeText(a, "Recordatorio cancelado",
                android.widget.Toast.LENGTH_SHORT).show();
        });
        LinearLayout.LayoutParams b2lp = new LinearLayout.LayoutParams(0,
            ViewGroup.LayoutParams.WRAP_CONTENT, 0.5f);
        b2lp.setMargins(Ui.dps(a, 10), 0, 0, 0);
        b2.setLayoutParams(b2lp);
        filaBtn.addView(b2);
        rec.addView(filaBtn);
        col.addView(rec);
        col.addView(Ui.espacio(a, 14));

        // ---------- Fecha de examen ----------
        LinearLayout fe = Ui.tarjeta(a);
        fe.addView(Ui.tv(a, "📅 Fecha del examen", 14, Ui.TEXT, true));
        fe.addView(Ui.espacio(a, 8));
        TextView actual = Ui.tv(a, "Actual: " + pr.fechaExamen(), 12, Ui.MUTED, false);
        fe.addView(actual);
        fe.addView(Ui.espacio(a, 10));
        android.widget.EditText campo = Ui.campo(a, "2026-12-15");
        campo.setText(pr.fechaExamen());
        fe.addView(campo, Ui.full(a));
        fe.addView(Ui.espacio(a, 10));
        fe.addView(Ui.boton(a, "Guardar fecha", 0xFFF1F5F9, Ui.TEXT, () -> {
            String v = campo.getText().toString().trim();
            if (v.matches("\\d{4}-\\d{2}-\\d{2}")) {
                pr.setFechaExamen(v);
                actual.setText("Actual: " + v);
                android.widget.Toast.makeText(a, "Fecha guardada",
                    android.widget.Toast.LENGTH_SHORT).show();
            } else {
                android.widget.Toast.makeText(a, "Formato: AAAA-MM-DD",
                    android.widget.Toast.LENGTH_SHORT).show();
            }
        }), Ui.full(a));
        col.addView(fe);
        col.addView(Ui.espacio(a, 14));

        // ---------- Plan de estudio ----------
        col.addView(Ui.boton(a, "📚 Ver plan de estudio hasta el examen",
            0xFFFFFFFF, Ui.PRIMARY, () -> act.navegar(new PantallaPlan(act))), Ui.full(a));
        col.addView(Ui.espacio(a, 10));

        // ---------- Información ----------
        LinearLayout inf = Ui.tarjeta(a);
        inf.addView(Ui.tv(a, "ℹ️ Sobre la app", 14, Ui.TEXT, true));
        inf.addView(Ui.espacio(a, 8));
        TextView det = Ui.tv(a,
            "Subalterno 81/26 · Generalitat Valenciana\n\n"
            + "Banco de " + Banco.todas().size() + " preguntas extraídas de los exámenes "
            + "oficiales publicados y del temario de la convocatoria. "
            + "Cada pregunta incluye la respuesta correcta y su explicación.\n\n"
            + "Todos los datos de progreso se guardan solo en este dispositivo.",
            11, Ui.MUTED, false);
        det.setLineSpacing(0, 1.5f);
        inf.addView(det);
        col.addView(inf);
        col.addView(Ui.espacio(a, 14));

        // ---------- Borrar progreso ----------
        col.addView(Ui.boton(a, "Reiniciar todo el progreso", 0xFFFEF2F2, Ui.KO, () -> {
            new android.app.AlertDialog.Builder(act)
                .setTitle("¿Reiniciar progreso?")
                .setMessage("Se borrarán todos tus aciertos, fallos y estadísticas. "
                    + "Esta acción no se puede deshacer.")
                .setPositiveButton("Borrar todo", (d, w) -> {
                    act.getSharedPreferences("subalterno_progreso", Context.MODE_PRIVATE)
                        .edit().clear().apply();
                    android.widget.Toast.makeText(act, "Progreso borrado",
                        android.widget.Toast.LENGTH_SHORT).show();
                    act.irTab(MainActivity.TAB_INICIO);
                })
                .setNegativeButton("Cancelar", null)
                .show();
        }), Ui.full(a));
    }

    // ---------- Programación del recordatorio ----------
    static void programar(Context c, int hora, int minuto) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        PendingIntent pi = pi(c);
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hora);
        cal.set(Calendar.MINUTE, minuto);
        cal.set(Calendar.SECOND, 0);
        if (cal.getTimeInMillis() <= System.currentTimeMillis())
            cal.add(Calendar.DAY_OF_MONTH, 1);

        if (Build.VERSION.SDK_INT >= 31 && am.canScheduleExactAlarms()) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        } else if (Build.VERSION.SDK_INT >= 23) {
            am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        } else {
            am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
        }
    }

    static void cancelar(Context c) {
        AlarmManager am = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
        am.cancel(pi(c));
    }

    private static PendingIntent pi(Context c) {
        Intent i = new Intent(c, RecordatorioReceiver.class);
        int flags = PendingIntent.FLAG_UPDATE_CURRENT;
        if (Build.VERSION.SDK_INT >= 23) flags |= PendingIntent.FLAG_IMMUTABLE;
        return PendingIntent.getBroadcast(c, 1001, i, flags);
    }
}
