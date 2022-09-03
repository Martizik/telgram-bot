package letscode.shiza;

import com.pengrad.telegrambot.Callback;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.InlineQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineQueryResultArticle;
import com.pengrad.telegrambot.request.AnswerInlineQuery;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.EditMessageText;
import com.pengrad.telegrambot.request.SendMessage;
import java.awt.event.ItemListener;


public class Bot {
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));
    private final String WAIT_LABEL = "Wait...";

    public void serve() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });


    }

    private void process(Update update) {
        Message message = update.message();
        CallbackQuery callbackQuery = update.callbackQuery();
        InlineQuery inlineQuery = update.inlineQuery();

        BaseRequest request = null;

        if (message != null && message.viaBot() != null && message.viaBot().username().equals("@testttGame_bot")) {
            InlineKeyboardMarkup replyMarkup = message.replyMarkup();
            if (replyMarkup == null){
                return;
            }
            InlineKeyboardButton[][] buttons = replyMarkup.inlineKeyboard();

            if (buttons == null){
                return;
            }

            InlineKeyboardButton button = buttons[0][0];
            String buttonLabel = button.text();

            if(!buttonLabel.equals(WAIT_LABEL)){
                return;
            }

            Long chatId = message.chat().id();
            String senderName = message.from().firstName();
            String senderChose = button.callbackData();
            Integer messageId = message.messageId();
            request = new EditMessageText(chatId, messageId, message.text())
                    .replyMarkup(
                            new InlineKeyboardMarkup(
                                    new InlineKeyboardButton("\uD83E\uDEA8")
                                            .callbackData(String.format("%d %s %s %s", chatId, senderName, senderChose, "0")),
                                    new InlineKeyboardButton("✂ ")
                                            .callbackData(String.format("%d %s %s %s", chatId, senderName, senderChose, "1")),
                                    new InlineKeyboardButton("\uD83D\uDCF0")
                                            .callbackData(String.format("%d %s %s %s", chatId, senderName, senderChose, "2"))

                            )
                    );


        } else if (inlineQuery != null){
            InlineQueryResultArticle stone = bildInlineButton("stone","\uD83E\uDEA8 Stone","0");
            InlineQueryResultArticle scissor = bildInlineButton("scissor","✂ Scissor","1");
            InlineQueryResultArticle paper = bildInlineButton("paper","\uD83D\uDCF0 Paper","2");

            request = new AnswerInlineQuery(inlineQuery.id(),stone,scissor,paper).cacheTime(1);

        } else if (callbackQuery != null) {
            String[] data = callbackQuery.data().split(" ");
            String chatId = data[0];
            String senderName = data[1];
            String senderChose = data[2];
            String opponentChose = data[3];
            String opponentName = callbackQuery.from().firstName();
            System.out.println("");
            
        }


        //else if(message != null) {
        //    long chatId = update.message().chat().id();
        //    request = new SendMessage(chatId,"Shalom");
        //}

         if (request != null) {
            bot.execute(request);
        }
    }

    private InlineQueryResultArticle bildInlineButton(String id, String title, String callbackData) {
        return new InlineQueryResultArticle(id, title, "I m ready to fight")
                .replyMarkup(
                        new InlineKeyboardMarkup(
                                new InlineKeyboardButton(WAIT_LABEL).callbackData("0"))
                );
    }
}
