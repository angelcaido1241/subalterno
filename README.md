# Subalterno GV — Oposición 81/26

App Android para preparar la oposición de **Subalterno de la Generalitat Valenciana**.
Examen: **sábado 14 de noviembre de 2026**.

## Contenido
- 136 preguntas de los 9 temas del temario, con explicación de cada respuesta
- Test por tema con corrección inmediata
- Simulacro cronometrado (3 opciones, los errores no penalizan)
- Repaso espaciado de fallos: 1, 3, 7, 15 y 30 días
- Estadísticas por tema y racha de días
- Plan de estudio en 4 fases hasta el examen
- Recordatorio diario

## Compilar el APK

### Opción 1: GitHub Actions (recomendado, sin instalar nada)
1. Crea un repositorio en GitHub y sube esta carpeta.
2. Ve a la pestaña **Actions** → workflow **Build APK Subalterno**.
3. Cuando termine, descarga el artifact **subalterno-apk**.
4. Instala el APK en el móvil (permitir "instalar apps desconocidas").

### Opción 2: Android Studio
1. Abre esta carpeta con Android Studio (Ladybug o superior).
2. `Build` → `Build Bundle(s)/APK(s)` → `Build APK(s)`.
3. El APK queda en `app/build/outputs/apk/debug/`.

### Opción 3: línea de comandos
```sh
./gradlew assembleDebug
```

## Firma
Por defecto se compila en **debug**, firmado con la clave de depuración del SDK.
Para firmar en release, coloca el keystore y exporta las variables:
```sh
export KS_FILE=release.jks
export KS_PASS=tu_password
export KEY_ALIAS=subalterno
export KEY_PASS=tu_password
./gradlew assembleRelease
```

## Estructura
```
app/src/main/java/com/omnibot/subalterno/
├── MainActivity.java        actividad principal, 4 pestañas
├── Banco.java               modelo Pregunta + carga del banco
├── BancoT1..T9.java         preguntas por tema
├── Progreso.java            persistencia, repaso espaciado, racha
├── Ui.java                  helpers de interfaz
├── PantallaInicio.java      cuenta atrás y accesos
├── PantallaTemas.java       listado de temas
├── PantallaTema.java        detalle del tema
├── PantallaTest.java        motor de test
├── PantallaResultado.java   resultado de la sesión
├── PantallaSimulacro.java   modos de simulacro
├── PantallaProgreso.java    estadísticas
├── PantallaPlan.java        plan de estudio
├── PantallaAjustes.java     ajustes y recordatorios
├── RecordatorioReceiver.java notificación diaria
└── Temas.java               nombres y resúmenes de los temas
```
