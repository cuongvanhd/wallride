create view movement as SELECT m.*,
  DATE_FORMAT(
  CASE SUBSTRING( td11_beginningymd, 5, 1 ) WHEN ''
  THEN
    CASE SUBSTRING( tm07_leagestart, 5, 1 ) WHEN ''
    THEN CONCAT( tm07_leagestart, '-01-01' )
    ELSE
      CASE SUBSTRING( tm07_leagestart, 8, 1 ) WHEN ''
      THEN CONCAT( tm07_leagestart, '-01' )
      ELSE tm07_leagestart
      END
    END
  ELSE
    CASE SUBSTRING( td11_beginningymd, 8, 1 ) WHEN ''
      THEN CONCAT( td11_beginningymd, '-01' )
      ELSE td11_beginningymd
    END
  END, '%Y-%m-%d') AS s,
  DATE_FORMAT(
    CASE SUBSTRING( td11_endymd, 5, 1 ) WHEN ''
    THEN
      CASE LEFT( td11_endymd, 4) WHEN LEFT( tm07_leageend, 4)
      THEN
        CASE SUBSTRING( tm07_leageend, 5, 1 ) WHEN ''
        THEN CONCAT( tm07_leageend, '-12-31' )
        ELSE
          CASE SUBSTRING( tm07_leageend, 8, 1 ) WHEN ''
          THEN DATE_SUB(DATE_ADD(DATE_FORMAT(CONCAT(tm07_leageend,'-01'),'%Y-%m-%d'), INTERVAL 1 MONTH),INTERVAL 1 DAY)
          ELSE tm07_leageend
          END
        END
      ELSE CONCAT(td11_endymd,'-12-31')
      END
    ELSE
      CASE SUBSTRING( td11_endymd, 8, 1 ) WHEN ''
      THEN DATE_SUB(DATE_FORMAT( CONCAT( td11_endymd,'-01'),'%Y-%m-%d'),INTERVAL 1 DAY)
      ELSE DATE_SUB(DATE_FORMAT(td11_endymd,'%Y-%m-%d'),INTERVAL 1 DAY)
      END
    END , '%Y-%m-%d') AS s2,
  DATE_FORMAT(
    CASE SUBSTRING(tm07_leagestart,5,1) WHEN ''
    THEN CONCAT(tm07_leagestart,'-01-01')
    ELSE CASE SUBSTRING(tm07_leagestart,8,1) WHEN ''
      THEN CONCAT(tm07_leagestart,'-01')
      ELSE tm07_leagestart END
      END , '%Y-%m-%d') AS seson1,
  DATE_FORMAT(
    CASE SUBSTRING(tm07_leageend,5,1) WHEN ''
    THEN CONCAT(tm07_leageend,'-12-31')
    ELSE CASE SUBSTRING(tm07_leageend,8,1) WHEN ''
      THEN DATE_SUB(DATE_ADD(DATE_FORMAT(CONCAT( tm07_leageend,'-01'),'%Y-%m-%d'),INTERVAL 1 MONTH),INTERVAL 1 DAY)
      ELSE tm07_leageend END
    END, '%Y-%m-%d') AS seson2
  FROM td11_movement as m
  LEFT JOIN tm07_leage as l
  ON m.td11_leageid = l.tm07_id;

create view player_news as
select item.id, item.published, item.permalink, item.title, feeds.title as source
   from antenna_items as item inner join antenna_feeds as feeds on item.feed_id = feeds.id
   order by item.published desc limit 10