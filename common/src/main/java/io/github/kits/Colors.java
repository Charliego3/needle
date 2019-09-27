package io.github.kits;

import io.github.kits.enums.Color;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static io.github.kits.enums.Color.BACKGROUND;
import static io.github.kits.enums.Color.BASE_PRE;
import static io.github.kits.enums.Color.BLUE;
import static io.github.kits.enums.Color.BOLD;
import static io.github.kits.enums.Color.BLUE_LESS;
import static io.github.kits.enums.Color.CANARY;
import static io.github.kits.enums.Color.CYAN;
import static io.github.kits.enums.Color.END;
import static io.github.kits.enums.Color.GRAY;
import static io.github.kits.enums.Color.GRAY_MORE;
import static io.github.kits.enums.Color.GREEN;
import static io.github.kits.enums.Color.RED;
import static io.github.kits.enums.Color.UNDER_LINE;
import static io.github.kits.enums.Color.WHITE;
import static io.github.kits.enums.Color.YELLOW_MORE;

/**
 * 字符串颜色工具类
 *
 * @author whimthen
 * @version 1.0.0
 * @since 1.0.0
 */
public class Colors {

    /**
     * 将字符串转换为蓝色
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toBule(String arg) {
        return convertToColor(BLUE.getContent(), arg, false);
    }

    /**
     * 将字符串转换为蓝色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toBuleBold(String arg) {
        return convertToColor(BLUE.getContent(), arg, true);
    }

    /**
     * 将字符串转换为白色
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toWhite(String arg) {
        return convertToColor(WHITE.getContent(), arg, false);
    }

    /**
     * 将字符串转换为白色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toWhiteBold(String arg) {
        return convertToColor(WHITE.getContent(), arg, true);
    }

    /**
     * 将字符串转换为黄色
     *
     * @param arg   需要转化的字符串
     * @return      转换后的字符串
     */
    public static String toYellow(String arg) {
        return convertToColor(YELLOW_MORE.getContent(), arg, false);
    }

    /**
     * 将字符串转换为黄色粗体
     *
     * @param arg   需要转化的字符串
     * @return      转换后的字符串
     */
    public static String toYellowBold(String arg) {
        return convertToColor(YELLOW_MORE.getContent(), arg, true);
    }

    /**
     * 将字符串转换为灰色
     *
     * @param arg   需要转化的字符串
     * @return      转换后的字符串
     */
    public static String toGray(String arg) {
        return convertToColor(GRAY.getContent(), arg, false);
    }

    /**
     * 将字符串转换为灰色粗体
     *
     * @param arg   需要转化的字符串
     * @return      转换后的字符串
     */
    public static String toGrayBold(String arg) {
        return convertToColor(GRAY.getContent(), arg, true);
    }

    /**
     * 将字符串转换为下划线的形式显示
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toUnderLine(String arg) {
        return convertToColor(UNDER_LINE.getContent(), arg, false);
    }

    /**
     * 将字符串转换为下划线的形式显示粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toUnderLineBold(String arg) {
        return convertToColor(UNDER_LINE.getContent(), arg, true);
    }

    /**
     * 将字符串转换为背景形式
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toBackground(String arg) {
        return convertToColor(BACKGROUND.getContent(), arg, false);
    }

    /**
     * 将字符串转换为背景形式粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toBackgroundBold(String arg) {
        return convertToColor(BACKGROUND.getContent(), arg, true);
    }

    /**
     * 将字符创转换为灰色
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toGrayMore(String arg) {
        return convertToColor(GRAY_MORE.getContent(), arg, false);
    }

    /**
     * 将字符创转换为灰色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toGrayMoreBold(String arg) {
        return convertToColor(GRAY_MORE.getContent(), arg, true);
    }

    /**
     * 将字符串转换为青色显示
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toCyan(String arg) {
        return convertToColor(CYAN.getContent(), arg, false);
    }

    /**
     * 将字符串转换为青色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toCyanBold(String arg) {
        return convertToColor(CYAN.getContent(), arg, true);
    }

    /**
     * 将字符串转换为红色
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toRed(String arg) {
        return convertToColor(RED.getContent(), arg, false);
    }

    /**
     * 将字符串转换为红色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toRedBold(String arg) {
        return convertToColor(RED.getContent(), arg, true);
    }

    /**
     * 将字符串转换为淡黄色
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toCanary(String arg) {
        return convertToColor(CANARY.getContent(), arg, false);
    }

    /**
     * 将字符串转换为淡黄色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toCanaryBold(String arg) {
        return convertToColor(CANARY.getContent(), arg, true);
    }

    /**
     * 将字符串转换为淡蓝色
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    public static String toBuleLess(String arg) {
        return convertToColor(BLUE_LESS.getContent(), arg, false);
    }

    public static String toGreenBold(String arg) {
        return convertToColor(GREEN.getContent(), arg, true);
    }

	public static String toGreen(String arg) {
		return convertToColor(GREEN.getContent(), arg, false);
	}

    /**
     * 将字符串转换为淡蓝色粗体
     *
     * @param arg   需要转换的字符串
     * @return      转换后的字符串
     */
    static String toBuleLessBold(String arg) {
        return convertToColor(BLUE_LESS.getContent(), arg, true);
    }

    private static String convertToColor(String color, String arg, boolean isBold) {
        StringBuilder colorArg = new StringBuilder(BASE_PRE.getContent()).append(isBold ? BOLD.getContent() : "");
        return Envs.isNotWin() ? colorArg.append(color).append(arg).append(END.getContent()).toString() : arg;
    }

    public static String convertToColorNotEnd(String color, String arg, boolean isBold) {
        StringBuilder colorArg = new StringBuilder(BASE_PRE.getContent()).append(isBold ? BOLD.getContent() : "");
        return Envs.isNotWin() ? colorArg.append(color).append(arg).toString() : arg;
    }

    /**
     * 随机颜色
     *
     * @param arg 字符串
     * @return 渲染后的字符
     */
    public static String random(String arg) {
        List<Color> colorEnums = Arrays.stream(Color.values())
                                       .filter(color -> !Arrays.asList("base", "bold", "background", "underLine", "white", "end", "black")
            .contains(color.getColor())).collect(Collectors.toList());
        Color colorEnum = colorEnums.get(new Random().nextInt(colorEnums.size()));
        return convertToColor(colorEnum.getContent(), arg, false);
    }

}
