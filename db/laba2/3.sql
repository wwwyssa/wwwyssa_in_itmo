SELECT COUNT(*) AS Уникальные_дни
FROM (
         SELECT DATE("КОГДА_СОЗДАЛ") AS дата_создания
         FROM "Н_ВЕДОМОСТИ"
         GROUP BY DATE("КОГДА_СОЗДАЛ")
     ) AS УникальныеДаты;