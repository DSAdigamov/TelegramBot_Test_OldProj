import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendSticker;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardRemove;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.api.objects.stickers.Sticker;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Bot extends TelegramLongPollingBot {

    private static final String TOKEN = "";
    private static final String USERNAME = "";

    public String getBotUsername() {
        return USERNAME;
    }

    public String getBotToken() {
        return TOKEN;
    }

    public HashMap<Long, Customer> session = new HashMap<>();


    @Override
    public void onUpdateReceived(Update update) {
        if (update.getMessage() != null && update.getMessage().hasText())
        {
            try {
                DataBase DB = new DataBase();

            Message message = update.getMessage();
            SendMessage sendMessage = new SendMessage();
            //если нет в сессии
            if (!session.containsKey(message.getChatId()))
            {
                session.put(message.getChatId(), new Customer(message.getChatId()));
                session.get(message.getChatId()).setUser_id(message.getChatId());
                if(!DB.CheckForCustomer(session.get(message.getChatId()).getUser_id()))
                {
                    session.get(message.getChatId()).setCount(5);
                }
            }
            //Вывод входящего сообщения
            System.out.println(message.getText());

                if (!DB.CheckForCustomer(session.get(message.getChatId()).getUser_id()))
                {


                    if (session.get(message.getChatId()).getCount() == 5) {
                        session.get(message.getChatId()).countMinus();
                        session.get(message.getChatId()).setUser_id(message.getChatId());


                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Добро пожалоать, это бот");

                        sendMessage.setReplyMarkup(getKeyboardRegistration());


                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }

                    }

                    if (message.getText().equals("Добавить пользователя")) {
                        sendMessage.setChatId(message.getChatId().toString());
                        session.get(message.getChatId()).countMinus();
                        sendMessage.setText("Введите свое имя");

                        ReplyKeyboardRemove remove = new ReplyKeyboardRemove();
                        remove.setRemoveKeyboard(true);
                        sendMessage.setReplyMarkup(remove);

                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        return;
                    }


                    if(session.get(message.getChatId()).getCount() == 3)
                    {
                        session.get(message.getChatId()).setName(message.getText());
                        session.get(message.getChatId()).countMinus();
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Введите номер телефона");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    if(session.get(message.getChatId()).getCount() == 2)
                    {
                        session.get(message.getChatId()).setPhone(message.getText());
                        session.get(message.getChatId()).countMinus();
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText("Введите промокод");
                        try {
                            execute(sendMessage);
                        } catch (TelegramApiException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    if(session.get(message.getChatId()).getCount() == 1)
                    {
                        session.get(message.getChatId()).countMinus();

                        switch (message.getText()) {
                            case default -> {
                                session.get(message.getChatId()).setCard_id(1);
                                sendMessage.setText("Сегодня вы без cкидочки, к сожалению :(");
                                sendMessage.setChatId(message.getChatId().toString());
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }


                            }
                            case ("Универ") -> {

                                session.get(message.getChatId()).setCard_id(2);
                                sendMessage.setChatId(message.getChatId().toString());
                                sendMessage.setText("скидка 10%");
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }

                            }
                            case ("Студент") -> {
                                session.get(message.getChatId()).setCard_id(3);
                                sendMessage.setText("Сочувству вам, конечно. Вот вам скидка 20 %");
                                sendMessage.setChatId(message.getChatId().toString());
                                SendSticker sendSticker = new SendSticker();
                                sendSticker.setSticker(new InputFile("CAACAgIAAxkBAAE47JZhwysPtCVxKUVQVyOs1lsCLTX58QACUwMAAlirqQh908zB3VmvZiME"));
                                sendSticker.setChatId(message.getChatId().toString());
                                try {
                                    execute(sendMessage);
                                    execute(sendSticker);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }

                            }
                        }
                        DB.addCustomer(session.get(message.getChatId()).getUser_id(),session.get(message.getChatId()).getCard_id(), session.get(message.getChatId()).getName(), session.get(message.getChatId()).getPhone());
                        System.out.println(session.get(message.getChatId()).getCount());
                    }

                }

                if(session.get(message.getChatId()).getOrdering())
                {
                    if(message.getText().equals("Закончить выбор"))
                    {
                        session.get(message.getChatId()).setOrdering(false);
                        printOrder(message);
                        return;
                    }
                    //свзять с базой
                    DB.InsertOrder(message.getText(), message.getChatId());
                    sendMessage.setChatId(message.getChatId().toString());
                    sendMessage.setText("Выберите что-нибудь ещё или завершите выбор");
                    execute(sendMessage);


                }

                if(session.get(message.getChatId()).getCount() == 0 && !session.get(message.getChatId()).getOrdering())
                {
                    switch (message.getText()) {
                        case ("Сотрудники") -> {
                            ArrayList<String> info = DB.ShowEmployee();
                            sendMessage.setChatId(message.getChatId().toString());
                            sendMessage.setDisableNotification(true);
                            for (String s : info) {
                                sendMessage.setText(s.toString());
                                try {
                                    execute(sendMessage);
                                } catch (TelegramApiException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        case ("О себе") -> {
                            Customer info = DB.ShowCustome(message.getChatId());
                            sendMessage.setChatId(message.getChatId().toString());
                            sendMessage.setText(String.format("Ваше имя: %s \nВаш телефон: %s \nВаш статус: %s", info.getName(), info.getPhone(), info.getType()));
                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                        case ("Показать меню") -> {
                            printMenu(message);
                            session.get(message.getChatId()).setOrdering(true);
                        }
                        case ("Заказы") ->
                            printOrder(message);

                        case default -> {
                            sendMessage.setChatId(message.getChatId().toString());
                            sendMessage.setText("Выберите действие");

                            sendMessage.setReplyMarkup(getKeyboardGlobal());

                            try {
                                execute(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (SQLException | TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }





    public ReplyKeyboardMarkup getKeyboardRegistration()
    {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Добавить пользователя");

        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(true);

        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getKeyboardGlobal()
    {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        row.add("Показать меню");
        row.add("Заказы");
        keyboard.add(row);
        row = new KeyboardRow();
        row.add("Сотрудники");
        row.add("О себе");
        keyboard.add(row);

        keyboardMarkup.setKeyboard(keyboard);

        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);


        return keyboardMarkup;
    }

    public ReplyKeyboardMarkup getKeyboardMenu() throws SQLException
    {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

        DataBase DB = new DataBase();
        ArrayList<ArrayList<String>> matrix;
        matrix = DB.ShowMenu();
        for (int i = 0; i < matrix.get(0).size(); i++)
        {
            KeyboardRow row = new KeyboardRow();
            row.add(matrix.get(0).get(i).toString());
            keyboard.add(row);
        }
        KeyboardRow row = new KeyboardRow();
        row.add("Закончить выбор");
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);


        return keyboardMarkup;
    }

    private void printOrder(Message message) throws  SQLException, TelegramApiException {
        DataBase DB = new DataBase();
        ArrayList<ArrayList<String>> matrix;
        matrix = DB.ShowOrder(message.getChatId());
        double sum = 0;
        StringBuilder menu = new StringBuilder();
        if(!matrix.get(0).isEmpty()) {
            menu.append("Вот ваш заказ: \n");
            for (int i = 0; i < matrix.get(0).size(); i++) {
                menu.append(matrix.get(0).get(i)).append(" [").append(matrix.get(1).get(i)).append("]       \n").append(matrix.get(2).get(i)).append("₽\n");
            }
            for (int i = 0; i < matrix.get(2).size(); i++) {
                sum += Double.parseDouble(matrix.get(2).get(i));
            }
            menu.append("\nОбщая сумма заказа: ").append(sum).append("₽");
            sum *= Double.parseDouble(matrix.get(3).get(0));
            menu.append("\nОбщая сумма заказа со скидкой: ").append(sum).append("₽\n");
        } else {
            System.out.println("Заказ пуст");
            menu.append("Вы ещё ничего не выбрали.");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(getKeyboardGlobal());
        sendMessage.setText(menu.toString());
        execute(sendMessage);
    }


    public void printMenu(Message message) throws SQLException, TelegramApiException {
        DataBase DB = new DataBase();
        ArrayList<ArrayList<String>> matrix;
        matrix = DB.ShowMenu();
        StringBuilder menu = new StringBuilder();

            for (int i = 0; i < matrix.get(0).size(); i++) {
                menu.append(matrix.get(0).get(i)).append(" ").append(matrix.get(1).get(i)).append("\n");
            }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setReplyMarkup(getKeyboardMenu());
        sendMessage.setText(menu.toString());
        execute(sendMessage);
    }






}