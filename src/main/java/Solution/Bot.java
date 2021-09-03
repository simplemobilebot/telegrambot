package Solution;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public final class Bot extends TelegramLongPollingBot {
    private final String BOT_TOKEN;
    private final String BOT_USERNAME;
    private final String ADMIN_CHAT;

    public Bot(String botToken, String botUserName, String adminChat) {
        BOT_TOKEN = botToken;
        BOT_USERNAME = botUserName;
        ADMIN_CHAT = adminChat;
    }

    @Override
    public String getBotUsername() {
        return BOT_USERNAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        // если сообщение из "админской группы", то
        if( (update.hasMessage()) &&
            (update.getMessage().getChatId().toString().equals(ADMIN_CHAT)) &&
            (update.getMessage().isReply()) ) {

            Message message = update.getMessage();
            String chatId = message.getReplyToMessage().getForwardFrom().getId().toString();

            // подпись
            String firstName = update.getMessage().getFrom().getFirstName();
            String lastName = update.getMessage().getFrom().getLastName();
            String userName;
            if ((firstName != null) && (lastName != null))
                userName = String.format("<b>%s %s</b>", firstName, lastName);
            else if (firstName != null)
                userName = String.format("<b>%s</b>", firstName);
            else
                userName = String.format("<b>%s</b>", lastName);

            if (message.hasText()) {
                SendMessage replyMessage = new SendMessage();
                replyMessage.enableHtml(true);
                replyMessage.setChatId(chatId);
                replyMessage.setText(String.format("%s\n%s", userName, message.getText()));

                try {
                    execute(replyMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasPhoto()) {         // Дописать для MediaGroup!
                SendPhoto sendPhotoMessage = new SendPhoto();

                String fileId = message.getPhoto().get(0).getFileId();
                String caption = message.getCaption();

                sendPhotoMessage.setChatId(chatId);
                sendPhotoMessage.setPhoto(new InputFile(fileId));
                sendPhotoMessage.setParseMode(ParseMode.HTML);
                if (caption != null)
                    sendPhotoMessage.setCaption(String.format("%s\n%s", userName, caption));
                else
                    sendPhotoMessage.setCaption(userName);

                try {
                    execute(sendPhotoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasVoice()) {
                SendVoice sendVoiceMessage = new SendVoice();

                String fileId = message.getVoice().getFileId();
                String caption = message.getCaption();

                sendVoiceMessage.setChatId(chatId);
                sendVoiceMessage.setVoice(new InputFile(fileId));
                sendVoiceMessage.setDuration(message.getVoice().getDuration());
                sendVoiceMessage.setParseMode(ParseMode.HTML);
                if (caption != null)
                    sendVoiceMessage.setCaption(String.format("%s\n%s", userName, caption));
                else
                    sendVoiceMessage.setCaption(userName);

                try {
                    execute(sendVoiceMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasVideo()) {
                SendVideo sendVideoMessage = new SendVideo();

                String fileId = message.getVideo().getFileId();
                String caption = message.getCaption();

                sendVideoMessage.setChatId(chatId);
                sendVideoMessage.setVideo(new InputFile(fileId));
                sendVideoMessage.setDuration(message.getVideo().getDuration());
                sendVideoMessage.setParseMode(ParseMode.HTML);
                if (caption != null)
                    sendVideoMessage.setCaption(String.format("%s\n%s", userName, caption));
                else
                    sendVideoMessage.setCaption(userName);

                try {
                    execute(sendVideoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasVideoNote()) {
                SendVideoNote sendVideoNoteMessage = new SendVideoNote();

                String fileId = message.getVideoNote().getFileId();

                sendVideoNoteMessage.setChatId(chatId);
                sendVideoNoteMessage.setVideoNote(new InputFile(fileId));
                sendVideoNoteMessage.setDuration(message.getVideoNote().getDuration());
                sendVideoNoteMessage.setLength(message.getVideoNote().getLength());

                try {
                    execute(sendVideoNoteMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasAnimation()) {
                SendAnimation sendAnimationMessage = new SendAnimation();

                String fileId = message.getAnimation().getFileId();
                String caption = message.getCaption();

                sendAnimationMessage.setChatId(chatId);
                sendAnimationMessage.setAnimation(new InputFile(fileId));
                sendAnimationMessage.setDuration(message.getAnimation().getDuration());
                sendAnimationMessage.setParseMode(ParseMode.HTML);
                if (caption != null)
                    sendAnimationMessage.setCaption(String.format("%s\n%s", userName, caption));
                else
                    sendAnimationMessage.setCaption(userName);

                try {
                    execute(sendAnimationMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasDocument()) {
                SendDocument sendDocumentMessage = new SendDocument();

                String fileId = message.getDocument().getFileId();
                String caption = message.getCaption();

                sendDocumentMessage.setChatId(chatId);
                sendDocumentMessage.setDocument(new InputFile(fileId));
                sendDocumentMessage.setParseMode(ParseMode.HTML);
                if (caption != null)
                    sendDocumentMessage.setCaption(String.format("%s\n%s", userName, caption));
                else
                    sendDocumentMessage.setCaption(userName);

                try {
                    execute(sendDocumentMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasContact()) {
                SendContact sendContactMessage = new SendContact();

                sendContactMessage.setChatId(chatId);
                sendContactMessage.setPhoneNumber(message.getContact().getPhoneNumber());
                sendContactMessage.setFirstName(message.getContact().getFirstName());
                if(message.getContact().getLastName() != null)
                    sendContactMessage.setLastName(message.getContact().getLastName());
                if(message.getContact().getVCard() != null)
                    sendContactMessage.setVCard(message.getContact().getVCard());

                try {
                    execute(sendContactMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasLocation()) {
                SendLocation sendLocationMessage = new SendLocation();

                Double latitude = message.getLocation().getLatitude();
                Double longitude = message.getLocation().getLongitude();

                sendLocationMessage.setChatId(chatId);
                sendLocationMessage.setLatitude(latitude);
                sendLocationMessage.setLongitude(longitude);

                try {
                    execute(sendLocationMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            } else if(message.hasDice()) {
                SendDice sendDiceMessage = new SendDice();
                sendDiceMessage.setChatId(chatId);
                sendDiceMessage.setEmoji(message.getDice().getEmoji());

                try {
                    execute(sendDiceMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }

        // * * * * *
        // если сообщение отправил не администратор
        if( (update.hasMessage()) && (!update.getMessage().getChatId().toString().equals(ADMIN_CHAT)) ) {
            if(update.getMessage().isCommand()) {
                if(!update.getMessage().getText().equals("/start")) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(update.getMessage().getChatId().toString());
                    sendMessage.setText("Извините, Вы не можете отправять командные сообщения!");
                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else {
                    // '/start' - TODO nothing
                }
            } else {
                Message message = update.getMessage();
                if(!isOnTime()) {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("Мы закрыты.\n" +
                            "График работы\n" +
                            "пн-пт: 10:00 — 20:00,\n" +
                            "сб: 11:00 — 17:00,\n" +
                            "вс: выходной\n" +
                            "Не расстраивайтесь, Вам ответят при первой же возможности.");

                    try {
                        execute(sendMessage);
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }

                ForwardMessage forwardMessage = new ForwardMessage();
                forwardMessage.setChatId(ADMIN_CHAT);
                forwardMessage.setFromChatId(update.getMessage().getChatId().toString());
                forwardMessage.setMessageId(update.getMessage().getMessageId());

                try {
                    execute(forwardMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // проверяет рабочее время
    public boolean isOnTime() {
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("Europe/Moscow"));

        int open = 10;
        int close = 20;

        int openSat = 11;
        int closeSat = 17;


        if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return false;
        } else if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            if( (calendar.get(Calendar.HOUR_OF_DAY) < openSat) || (calendar.get(Calendar.HOUR_OF_DAY) > closeSat - 1))
                return false;
        } else if( (calendar.get(Calendar.HOUR_OF_DAY) < open) || (calendar.get(Calendar.HOUR_OF_DAY) > close - 1)) {
            return false;
        }

        return true;
    }
}
