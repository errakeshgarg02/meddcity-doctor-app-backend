/*
 * arg license
 *
 */

package com.arg.common.utils;

import java.io.IOException;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.util.ObjectUtils;

public class CommonUtil {

    public static DateTimeFormatter dateFormatter_DD_MM_YYYY = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static List<String> trim(List<String> list) {
        if (ObjectUtils.isEmpty(list)) {
            return list;
        }
        return list.stream().map(CommonUtil::trimString).filter(str -> str.length() > 0).collect(Collectors.toList());

    }

    public static String trimString(String data) {
        String replace = data.replace("{", "");
        replace = replace.replace("}", "");
        replace = replace.replaceAll("^\"+|\"+$", "");
        return replace.trim();
    }

    public static LocalDate parseDate(String date, DateTimeFormatter dateTimeFormatter) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        return LocalDate.parse(date, dateTimeFormatter);
    }

    public static String formatDate(LocalDate date, DateTimeFormatter dateTimeFormatter) {
        if (ObjectUtils.isEmpty(date)) {
            return null;
        }
        return date.format(dateTimeFormatter);
    }

    public static Path writeFileContent(FilePart filePart) {
        Path tempFile = null;
        AsynchronousFileChannel channel = null;
        try {
            tempFile = Files.createTempFile("", "_" + filePart.filename());
            channel = AsynchronousFileChannel.open(tempFile, StandardOpenOption.WRITE);

        } catch (IOException e1) {
            e1.printStackTrace();
        }

        DataBufferUtils.write(filePart.content(), channel, 0).doOnComplete(() -> {
            System.out.println("finish");

        }).subscribe();
        filePart.transferTo(tempFile.toFile());
        return tempFile;
    }
}
