package com.omnibot.subalterno;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Persistencia local: progreso por pregunta, repaso espaciado tipo SM-2
 * simplificado, racha de estudio y estadísticas.
 */
public final class Progreso {

    private final SharedPreferences sp;

    // claves por pregunta: id#aciertos, id#fallos, id#caja, id#visto
    private static final String K_ACIERTOS = "a_";
    private static final String K_FALLOS = "f_";
    private static final String K_CAJA = "c_";
    private static final String K_DIAS = "d_";
    private static final String K_DOMINADA = "m_";

    // claves generales
    private static final String K_ULTIMO_DIA = "ultimo_dia";
    private static final String K_RACHA = "racha";
    private static final String K_FECHA_EXAMEN = "fecha_examen";
    private static final String K_TEST_HECHOS = "test_hechos";
    private static final String K_SIM_HECHOS = "sim_hechos";
    private static final String K_MINUTOS = "minutos";

    public Progreso(Context c) {
        sp = c.getSharedPreferences("subalterno_progreso", Context.MODE_PRIVATE);
    }

    // ---------- Fecha de examen ----------
    public String fechaExamen() {
        return sp.getString(K_FECHA_EXAMEN, "2026-11-14");
    }

    public void setFechaExamen(String iso) {
        sp.edit().putString(K_FECHA_EXAMEN, iso).apply();
    }

    // ---------- Registro de respuestas ----------
    public void registrar(Banco.Pregunta q, boolean acierto) {
        String id = q.id();
        if (acierto) {
            int a = sp.getInt(K_ACIERTOS + id, 0) + 1;
            int caja = Math.min(5, sp.getInt(K_CAJA + id, 0) + 1);
            sp.edit()
              .putInt(K_ACIERTOS + id, a)
              .putInt(K_CAJA + id, caja)
              .putInt(K_DIAS + id, intervaloDias(caja))
              .putLong(K_DIAS + id + "_t", System.currentTimeMillis())
              .apply();
            if (caja >= 4) sp.edit().putBoolean(K_DOMINADA + id, true).apply();
        } else {
            int f = sp.getInt(K_FALLOS + id, 0) + 1;
            sp.edit()
              .putInt(K_FALLOS + id, f)
              .putInt(K_CAJA + id, 0)
              .putInt(K_DIAS + id, 1)
              .putLong(K_DIAS + id + "_t", System.currentTimeMillis())
              .putBoolean(K_DOMINADA + id, false)
              .apply();
            anotarFallo(id);
        }
        marcarDiaEstudiado();
    }

    private static int intervaloDias(int caja) {
        switch (caja) {
            case 1: return 1;
            case 2: return 3;
            case 3: return 7;
            case 4: return 15;
            case 5: return 30;
            default: return 1;
        }
    }

    public int aciertos(Banco.Pregunta q) { return sp.getInt(K_ACIERTOS + q.id(), 0); }
    public int fallos(Banco.Pregunta q) { return sp.getInt(K_FALLOS + q.id(), 0); }
    public int caja(Banco.Pregunta q) { return sp.getInt(K_CAJA + q.id(), 0); }
    public boolean dominada(Banco.Pregunta q) { return sp.getBoolean(K_DOMINADA + q.id(), false); }
    public boolean vista(Banco.Pregunta q) {
        return aciertos(q) + fallos(q) > 0;
    }

    /** ¿Toca repasar esta pregunta hoy? */
    public boolean pendienteHoy(Banco.Pregunta q) {
        if (!vista(q)) return true;
        long dias = sp.getInt(K_DIAS + q.id(), 1);
        long t = sp.getLong(K_DIAS + q.id() + "_t", 0L);
        long transcurrido = (System.currentTimeMillis() - t) / 86400000L;
        return transcurrido >= dias;
    }

    // ---------- Lista de fallos (para repaso dirigido) ----------
    private void anotarFallo(String id) {
        Set<String> fallos = new HashSet<>(sp.getStringSet("lista_fallos", new HashSet<String>()));
        fallos.add(id);
        sp.edit().putStringSet("lista_fallos", fallos).apply();
    }

    public void quitarFallo(String id) {
        Set<String> fallos = new HashSet<>(sp.getStringSet("lista_fallos", new HashSet<String>()));
        fallos.remove(id);
        sp.edit().putStringSet("lista_fallos", fallos).apply();
    }

    public List<Banco.Pregunta> preguntasFalladas() {
        List<Banco.Pregunta> out = new ArrayList<>();
        for (String id : sp.getStringSet("lista_fallos", new HashSet<String>())) {
            Banco.Pregunta q = Banco.porId(id);
            if (q != null && !dominada(q)) out.add(q);
        }
        return out;
    }

    // ---------- Racha y actividad ----------
    public void marcarDiaEstudiado() {
        String hoy = hoyISO();
        String ultimo = sp.getString(K_ULTIMO_DIA, "");
        if (hoy.equals(ultimo)) return;
        int racha = sp.getInt(K_RACHA, 0);
        if (esAyer(ultimo, hoy)) racha = racha + 1; else racha = 1;
        sp.edit().putString(K_ULTIMO_DIA, hoy).putInt(K_RACHA, racha).apply();
    }

    public int racha() {
        String ultimo = sp.getString(K_ULTIMO_DIA, "");
        String hoy = hoyISO();
        if (ultimo.equals(hoy) || esAyer(ultimo, hoy)) return sp.getInt(K_RACHA, 0);
        return 0;
    }

    public boolean estudiadoHoy() { return hoyISO().equals(sp.getString(K_ULTIMO_DIA, "")); }

    public void sumarTest() { sp.edit().putInt(K_TEST_HECHOS, sp.getInt(K_TEST_HECHOS, 0) + 1).apply(); }
    public void sumarSimulacro() { sp.edit().putInt(K_SIM_HECHOS, sp.getInt(K_SIM_HECHOS, 0) + 1).apply(); }
    public int testsHechos() { return sp.getInt(K_TEST_HECHOS, 0); }
    public int simulacrosHechos() { return sp.getInt(K_SIM_HECHOS, 0); }

    public void sumarMinutos(int min) {
        sp.edit().putInt(K_MINUTOS, sp.getInt(K_MINUTOS, 0) + min).apply();
    }

    public int minutosTotales() { return sp.getInt(K_MINUTOS, 0); }

    // ---------- Estadísticas globales ----------
    public int totalVistas() {
        int n = 0;
        for (Banco.Pregunta q : Banco.todas()) if (vista(q)) n++;
        return n;
    }

    public int totalDominadas() {
        int n = 0;
        for (Banco.Pregunta q : Banco.todas()) if (dominada(q)) n++;
        return n;
    }

    public int pendientesHoy() {
        int n = 0;
        for (Banco.Pregunta q : Banco.todas()) if (pendienteHoy(q)) n++;
        return n;
    }

    /** Porcentaje de acierto global. */
    public int porcentajeGlobal() {
        int ok = 0, tot = 0;
        for (Banco.Pregunta q : Banco.todas()) {
            ok += aciertos(q);
            tot += aciertos(q) + fallos(q);
        }
        return tot == 0 ? 0 : Math.round(ok * 100f / tot);
    }

    /** Datos por tema: {vistas, dominadas, total, porcentajeAcierto}. */
    public int[][] porTema() {
        int[][] r = new int[10][4];
        for (Banco.Pregunta q : Banco.todas()) {
            int t = q.tema;
            r[t][2]++;
            if (vista(q)) r[t][0]++;
            if (dominada(q)) r[t][1]++;
            r[t][3] += aciertos(q);
        }
        for (int t = 1; t <= 9; t++) {
            int tot = 0;
            for (Banco.Pregunta q : Banco.delTema(t)) tot += aciertos(q) + fallos(q);
            r[t][3] = tot == 0 ? 0 : Math.round(r[t][3] * 100f / tot);
        }
        return r;
    }

    // ---------- Selección para sesiones ----------
    public List<Banco.Pregunta> seleccionAleatoria(int tema, int n) {
        List<Banco.Pregunta> base = tema == 0 ? new ArrayList<>(Banco.todas()) : Banco.delTema(tema);
        Collections.shuffle(base, new java.util.Random());
        return base.subList(0, Math.min(n, base.size()));
    }

    /** Preguntas que toca repasar hoy, priorizando fallos. */
    public List<Banco.Pregunta> seleccionRepaso(int n) {
        List<Banco.Pregunta> falladas = preguntasFalladas();
        List<Banco.Pregunta> resto = new ArrayList<>();
        for (Banco.Pregunta q : Banco.todas()) {
            if (!dominada(q) && pendienteHoy(q) && !falladas.contains(q)) resto.add(q);
        }
        Collections.shuffle(resto, new java.util.Random());
        List<Banco.Pregunta> out = new ArrayList<>();
        for (Banco.Pregunta q : falladas) { if (out.size() < n) out.add(q); }
        for (Banco.Pregunta q : resto) { if (out.size() < n) out.add(q); }
        if (out.isEmpty()) out.addAll(seleccionAleatoria(0, n));
        return out;
    }

    // ---------- utilidades de fecha ----------
    public static String hoyISO() {
        return new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            .format(new java.util.Date());
    }

    private static boolean esAyer(String ultimo, String hoy) {
        try {
            java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.util.Date d1 = f.parse(ultimo);
            java.util.Date d2 = f.parse(hoy);
            long dif = (d2.getTime() - d1.getTime()) / 86400000L;
            return dif == 1;
        } catch (Exception e) {
            return false;
        }
    }

    public static long diasHasta(String iso) {
        try {
            java.text.SimpleDateFormat f = new java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US);
            java.util.Date fin = f.parse(iso);
            long ms = fin.getTime() - System.currentTimeMillis();
            return Math.max(0, ms / 86400000L);
        } catch (Exception e) {
            return 0;
        }
    }

    public Map<String, Integer> fallosPorTema() {
        Map<String, Integer> m = new HashMap<>();
        for (Banco.Pregunta q : Banco.todas()) {
            if (fallos(q) > 0) {
                String k = "T" + q.tema;
                Integer v = m.get(k);
                m.put(k, v == null ? 1 : v + 1);
            }
        }
        return m;
    }
}
