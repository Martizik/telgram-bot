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
import com.pengrad.telegrambot.request.SendMessage;

import java.awt.event.ItemListener;


public class Bot {
    private final TelegramBot bot = new TelegramBot(System.getenv("BOT_TOKEN"));

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

        if (inlineQuery != null){
            InlineQueryResultArticle stone = bildInlineButton("stone","Stone","0");
            InlineQueryResultArticle scissor = bildInlineButton("scissor","Scissor","1");
            InlineQueryResultArticle paper = bildInlineButton("paper","Paper","2");

            request= new AnswerInlineQuery(inlineQuery.id(),stone,scissor,paper);

        }

        else if(message != null) {
            long chatId = update.message().chat().id();
            request = new SendMessage(chatId,"Shalom");
        }

        if (request != null) {
            bot.execute(request);
        }
    }

    private InlineQueryResultArticle bildInlineButton(String id, String title, String callbackData) {
        return new InlineQueryResultArticle(id, title, "I m ready to fight")
                .replyMarkup(
                        new InlineKeyboardMarkup(
                                new InlineKeyboardButton("Wait...").callbackData("0"))
                );
    }
}
