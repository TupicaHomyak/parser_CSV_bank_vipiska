import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) {
        String path = "data\\movementList.csv"; // Путь к файлу со списком
        List<String> lines = new ArrayList<>(); // Список для хранения строк из файла
        try { // Считываем все строки из файла в список lines
            lines = Files.readAllLines(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Хэш-отображение для хранения типов платежей и сумм расходов:
        HashMap<String, Double> expence2sum = new HashMap<>();
        // Переменная для хранения первой строки (заголовка)
        String firstLine = null;

        // Проходим по каждой строке в списке lines
        for (String line : lines) {
            // Если первая строка пуста, сохраняем ее и переходим к следующей строке
            if (firstLine == null) {
                firstLine = line;
                continue;
            }
            // Разделяем строку на подстроки, используя запятую в качестве разделителя:
            String[] tokens = line.split(",");
            // Получаем значение расхода в виде числа из 7-го элемента подстроки
            double expense = Double.parseDouble(tokens[7]);
            // Если расход равен 0, пропускаем строку
            if (expense == 0) {
                continue;
            }
            // Получаем тип платежа из 5-го элемента подстроки, используя метод getPaymentType()
            String paymentType = getPaymentType(tokens[5]);
            // Если тип платежа в хэш-отображении отсутствует, добавляем его со значением 0.0
            if (!expence2sum.containsKey(paymentType)) {
                expence2sum.put(paymentType, 0.);
            }
            // Увеличиваем сумму расходов для данного типа платежа
            expence2sum.put(paymentType, expence2sum.get(paymentType) + expense);
        }
        // Выводим результаты на консоль
        for (String paymentType : expence2sum.keySet()) {
            double sum = expence2sum.get(paymentType);
            System.out.println(paymentType + "\t" + sum);
        }
    }
    private static String getPaymentType(String info) {
        String regex = "[^a-zA-Z0-9]([a-zA-Z0-9\s]+)[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}\s[0-9]{2}\\.[0-9]{2}\\.[0-9]{2}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(info);
        return matcher.find() ? matcher.group(1).trim() :
                null;

    }
}