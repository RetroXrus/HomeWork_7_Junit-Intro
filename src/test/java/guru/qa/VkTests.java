package guru.qa;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class VkTests {

    @BeforeAll
    static void configure() {
        Configuration.browser = "chrome";
        Configuration.browserSize = "2560x1440";
    }

    //Из-за особенностей движка поиска в VK данный тест проходит не всегда - изредка выдает не самые релевантные результаты
    @ValueSource(strings = {"Маша и Медведь", "IGN Россия"})
    @ParameterizedTest(name = "Unregistered user is able to find group {0} via search and open it")
    void searchOpenGroupTest(String testData) {
        open("https://vk.com");
        $("#ts_input").setValue(testData).pressEnter();
        $$(".groups_row").first().shouldHave(text(testData));
        $$(".groups_row").first().click();
        $("#page_wall_posts").shouldHave(text(testData));
    }

    @CsvSource(value = {
            "https://vk.com,  Назад",
            "https://vk.ru,  Назад",
    })
    @ParameterizedTest(name = "Button \"{1}\" is available on website \"{0}\"")
    void visibilityOfBackButtonOnMainPageTest(String testLink, String expectedResult) {
        open(testLink);
        $("#ts_input").setValue("QA").pressEnter();
        sleep(500); //Без задержки на этом этапе переход на Главную срабатывает настолько быстро, что кнопка "Назад" не появляется
        $(".TopHomeLink").click();
        $("#stl_text").shouldHave(text(expectedResult));
    }

    static Stream<Arguments> vkSiteMenuTest() {
        return Stream.of(
                Arguments.of(Lang.Русский, List.of("Вход ВКонтакте\n" +
                        "Чужой компьютер\n" +
                        "Войти\n" +
                        "Или\n" +
                        "QR-код\n" +
                        "Зарегистрироваться\n" +
                        "После регистрации вы получите доступ ко всем возможностям VK ID\n" +
                        "Узнать больше")),
                Arguments.of(Lang.Українська, List.of("Вхід у VK\n" +
                        "Чужий комп'ютер\n" +
                        "Увійти\n" +
                        "або\n" +
                        "QR-код\n" +
                        "Зареєструватися\n" +
                        "Після реєстрації ви отримаєте доступ до всіх можливостей VK ID\n" +
                        "Дізнатися більше")),
                Arguments.of(Lang.English, List.of("Sign in to VK\n" +
                        "Don't remember me\n" +
                        "Sign in\n" +
                        "Or\n" +
                        "QR code\n" +
                        "Sign up\n" +
                        "After signing up, you'll get access to all of VK ID's features\n" +
                        "Learn more"))
        );
    }
    @MethodSource
    @ParameterizedTest(name = "Localization: {0}; Visible text: {1}")
    void vkSiteMenuTest(Lang lang, List<String> expectedText) {
        open(Environment.COM.getUrl());
        $$(".footer_lang a").find(text(lang.name())).click();
        $$("#index_rcolumn").shouldHave(CollectionCondition.texts(expectedText));
        open(Environment.RU.getUrl());
        $$(".footer_lang a").find(text(lang.name())).click();
        $$("#index_rcolumn").shouldHave(CollectionCondition.texts(expectedText));
    }
}
