select users.name, messages.date, messages.text 
from (messages inner join users on messages.idUsers = users.id)
having length(messages.text) > 20;