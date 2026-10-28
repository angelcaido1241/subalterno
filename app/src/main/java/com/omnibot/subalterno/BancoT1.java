package com.omnibot.subalterno;

/** Tema 1 · La Constitución Española de 1978 (26 preguntas). */
final class BancoT1 {

    interface Add {
        void p(int tema, String en, String a, String b, String c, int ok, String exp);
    }

    static void cargar(Add add) {
        add.p(1, "¿En qué fecha se votó a favor de la Constitución Española?",
          "El 6 de diciembre de 1878", "El 6 de diciembre de 1978", "El 6 de diciembre de 1987", 1,
          "Los españoles votaron a favor de la Constitución el 6 de diciembre de 1978, y por eso ese día es fiesta.");
        add.p(1, "¿Cuáles son los valores más importantes de la Constitución?",
          "Libertad, justicia, igualdad y respeto a las distintas ideas políticas",
          "Democracia, capitalismo y bienestar social",
          "Obediencia, igualdad, conciencia política y socialismo", 0,
          "Los valores superiores son la libertad, la justicia, la igualdad y el pluralismo político.");
        add.p(1, "Señala la respuesta correcta sobre la soberanía nacional:",
          "Es el poder que ejercen los partidos políticos sobre el pueblo",
          "Proviene del poder de la monarquía sobre el pueblo",
          "Es el poder de las instituciones que proviene del pueblo español", 2,
          "La soberanía nacional reside en el pueblo español, del que emanan los poderes del Estado.");
        add.p(1, "¿A qué se refiere el derecho al honor?",
          "A tener ayuda de un abogado o abogada que me defienda",
          "A que los demás me respeten y no me insulten",
          "A tener las ideas políticas y la religión que quiera", 1,
          "El derecho al honor protege la dignidad de la persona frente a insultos y descalificaciones.");
        add.p(1, "¿Qué es la libertad de ideas?",
          "Poder elegir el lugar donde quiero vivir",
          "Tener las ideas políticas y la religión que quiera",
          "Derecho a reunirme sin armas y sin violencia", 1,
          "La libertad ideológica y religiosa permite tener las creencias políticas y religiosas que se quieran.");
        add.p(1, "¿Cuál es el derecho de las personas detenidas por la policía?",
          "Ser detenidas sin explicación",
          "Guardar silencio y tener un abogado",
          "Ser condenadas inmediatamente", 1,
          "Toda persona detenida tiene derecho a guardar silencio y a la asistencia de un abogado. La detención preventiva dura como máximo 72 horas.");
        add.p(1, "¿Cuáles son los límites en cuanto a la expresión de ideas propias?",
          "El respeto al derecho de las demás personas",
          "No hay ningún límite",
          "Los dictados por un juez", 0,
          "La libertad de expresión tiene como límite el respeto a los derechos de los demás.");
        add.p(1, "Menciona una razón por la que las instituciones pueden prohibir una manifestación:",
          "Porque no están de acuerdo con la manifestación",
          "En caso de correr riesgo las personas o los bienes",
          "Si un partido político se niega a que las personas se manifiesten", 1,
          "Solo se puede prohibir o limitar cuando exista riesgo para personas o bienes.");
        add.p(1, "¿Cuáles de estas asociaciones están prohibidas en España?",
          "Las que buscan beneficios económicos legales",
          "Las que tienen intenciones contrarias a la ley",
          "Las que apoyan a personas inmigrantes", 1,
          "Están prohibidas las asociaciones secretas y las que forman grupos armados o tienen fines contrarios a la ley.");
        add.p(1, "¿De qué manera pueden los españoles participar en política?",
          "Manifestándose en contra de las políticas públicas",
          "Compartiendo su opinión de los diferentes partidos políticos",
          "Votando a sus representantes y/o presentando su candidatura", 2,
          "La participación política se ejerce votando y presentándose como candidato o candidata.");
        add.p(1, "Son asociaciones que defienden los derechos de las personas trabajadoras:",
          "Los partidos políticos", "Los sindicatos", "Las organizaciones no gubernamentales", 1,
          "Los sindicatos son las organizaciones que defienden los derechos de los trabajadores y trabajadoras.");
        add.p(1, "Es una institución que agrupa a los trabajadores de una profesión y marca normas sobre esa profesión:",
          "Las instituciones públicas", "La Seguridad Social", "Los colegios profesionales", 2,
          "Los colegios profesionales agrupan a las personas de una misma profesión y fijan sus normas.");
        add.p(1, "¿Qué institución está representada por el Rey?",
          "La presidencia del Gobierno", "La Corona", "Los ministerios", 1,
          "El Rey es el Jefe del Estado y representa a la Corona.");
        add.p(1, "Las Cortes Generales están formadas por:",
          "Los ministerios", "La Corona y la Presidencia", "El Congreso de los Diputados y el Senado", 2,
          "Las Cortes Generales se componen del Congreso de los Diputados y del Senado.");
        add.p(1, "¿Quién elige a los Diputados y Senadores en las elecciones generales?",
          "El Presidente del Gobierno", "La ciudadanía", "El Rey", 1,
          "Los diputados y senadores son elegidos por la ciudadanía en las elecciones generales.");
        add.p(1, "¿Quién elige al Presidente del Gobierno?",
          "El Rey", "La ciudadanía", "El Congreso de los Diputados", 2,
          "El Congreso de los Diputados elige al Presidente del Gobierno.");
        add.p(1, "¿Quién elige a los ministros y ministras?",
          "El Presidente del Gobierno", "La ciudadanía", "El Rey", 0,
          "Los ministros y ministras son nombrados a propuesta del Presidente del Gobierno.");
        add.p(1, "Está formado por el Presidente del Gobierno y los ministros:",
          "El Gobierno", "El Congreso", "El Poder Judicial", 0,
          "El Gobierno está compuesto por el Presidente, los Vicepresidentes y los Ministros.");
        add.p(1, "¿Quién forma el Poder Judicial?",
          "El Congreso de los Diputados y el Senado",
          "Jueces y Magistrados",
          "El Rey y el Presidente del Gobierno", 1,
          "El Poder Judicial está formado por jueces y magistrados, que juzgan y hacen ejecutar lo juzgado.");
        add.p(1, "¿Qué hace el Tribunal Constitucional?",
          "Puede anular las leyes contrarias a la Constitución",
          "Dirigir las instituciones de Gobierno",
          "Juzgar y hacer ejecutar lo juzgado", 0,
          "El Tribunal Constitucional vela por el cumplimiento de la Constitución y anula las leyes que la contradicen.");
        add.p(1, "¿Quién es el máximo representante de España?",
          "El Presidente del Gobierno", "El Consejo de Gobierno", "El Rey", 2,
          "El Rey es el Jefe del Estado y el máximo representante de España.");
        add.p(1, "¿A qué edad se alcanza la mayoría de edad en España?",
          "A los 16 años", "A los 18 años", "A los 21 años", 1,
          "La mayoría de edad se alcanza a los 18 años.");
        add.p(1, "¿Cuál es la duración máxima de la detención preventiva?",
          "24 horas", "48 horas", "72 horas (3 días)", 2,
          "La detención preventiva no puede durar más de 72 horas sin pasar a disposición judicial.");
        add.p(1, "La bandera de España tiene tres franjas horizontales de color:",
          "Roja, amarilla y roja", "Amarilla, roja y amarilla", "Azul, blanca y roja", 0,
          "La bandera de España tiene tres franjas horizontales: roja, amarilla y roja, siendo la amarilla el doble de ancha.");
        add.p(1, "La capital de España es:",
          "Barcelona", "Madrid", "València", 1,
          "La capital del Estado español es Madrid.");
        add.p(1, "¿Qué deber tienen los ciudadanos para financiar los servicios públicos?",
          "Pagar impuestos según sus ingresos", "No pagar nada", "Pagar todos la misma cantidad", 0,
          "El deber de contribuir se cumple pagando impuestos según la capacidad económica de cada persona.");
        add.p(1, "La educación básica en España es:",
          "Obligatoria y gratuita", "Solo gratuita", "Voluntaria y de pago", 0,
          "La Constitución reconoce el derecho a la educación y establece que la enseñanza básica es obligatoria y gratuita.");
        add.p(1, "En España está prohibida:",
          "La pena de muerte, salvo en tiempo de guerra", "La pena de muerte en todo caso",
          "Solo la tortura, pero no la pena de muerte", 1,
          "Queda abolida la pena de muerte, salvo lo que puedan disponer las leyes penales militares para tiempos de guerra; en todo caso está prohibida la tortura y las penas o tratos inhumanos.");
        add.p(1, "¿Qué derecho tienen las personas reclusas en prisión según la Constitución?",
          "A trabajar y cobrar un sueldo y a acceder a la cultura",
          "Ninguno, pierden todos sus derechos",
          "Solo a recibir visitas", 0,
          "Las personas condenadas a prisión conservan sus derechos fundamentales, incluido el derecho al trabajo remunerado y a la cultura.");
    }

    private BancoT1() {}
}
