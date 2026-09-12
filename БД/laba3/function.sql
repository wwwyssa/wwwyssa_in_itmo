CREATE OR REPLACE FUNCTION log_emotional_change()
RETURNS TRIGGER AS $$
DECLARE
    person_name VARCHAR(100);
    message_text TEXT;
BEGIN

    -- Получаем имя человека из таблицы person
    SELECT CONCAT(name, ' ', surname) INTO person_name
    FROM person
    WHERE id = NEW.person_id;
    
    -- Формируем текст сообщения в зависимости от типа изменения
    IF TG_OP = 'INSERT' THEN
        message_text := person_name || ' has new emotional status: ' || NEW.emotion_status;
    ELSIF TG_OP = 'UPDATE' AND OLD.emotion_status IS DISTINCT FROM NEW.emotion_status THEN
        message_text := person_name || ' changed emotional status from ' || OLD.emotion_status || 
                         ' to ' || NEW.emotion_status;
    ELSE
        RETURN NEW; -- Ничего не делаем, если статус не изменился
    END IF;
    
    -- Вставляем уведомление в таблицу message
    INSERT INTO message (content, sender_id, receiver_id, sent_time)
    VALUES (message_text, NEW.person_id, 4, NOW()); -- Отправляем системному пользователю (id=4)
    
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER emotional_status_change_trigger
AFTER INSERT OR UPDATE ON emotionalstatus
FOR EACH ROW
EXECUTE FUNCTION log_emotional_change();
