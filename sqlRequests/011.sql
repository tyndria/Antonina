select datediff(
curtime(), 
(select dateText as FirstDate from messages order by dateText limit 1)
) 
as NumberOfDays;