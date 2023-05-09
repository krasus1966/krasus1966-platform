package top.krasus1966.resource.test;

import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.office.InstalledOfficeManagerHolder;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.LocalConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.jodconverter.local.office.LocalOfficeUtils;
import org.springframework.boot.convert.DurationStyle;

import java.io.File;

/**
 * @author Krasus1966
 * @date 2023/5/6 22:32
 **/
@Slf4j
public class OfficeConvertMain {
    public static void main(String[] args) throws OfficeException {
        startOfficeManager();
        wordConvert2PDF("/Users/krasus1966/Public/陈旭的报名表.xlsx",
                "/Users/krasus1966/天拓网络temp/",
                "07.04.pdf");
        OfficeManager instance = InstalledOfficeManagerHolder.getInstance();
        instance.stop();
    }

    public static void startOfficeManager() throws OfficeException {
        File officeHome = LocalOfficeUtils.getDefaultOfficeHome();
        if (officeHome == null) {
            throw new RuntimeException("找不到office组件，请确认'office.home'配置是否有误");
        }
        try {
            long timeout = DurationStyle.detectAndParse("5m").toMillis();
            LocalOfficeManager officeManager = LocalOfficeManager.builder()
                    .officeHome(officeHome)
                    .portNumbers(8890)
                    .processTimeout(timeout)
                    .build();
            officeManager.start();
            InstalledOfficeManagerHolder.setInstance(officeManager);
        } catch (Exception e) {
            log.error("启动office组件失败，请检查office组件是否可用");
            throw e;
        }
    }

    public static void wordConvert2PDF(String filePath, String outputPath,String outputName) throws OfficeException {
        if (null != filePath) {
            File inputFile = new File(filePath);
            // 判断目标文件路径是否为空
            if (null == outputPath) {
                // 转换后的文件路径
                if (inputFile.exists()) {
                    // 找不到源文件, 则返回

                }
            } else {
                if (inputFile.exists()) {
                    // 找不到源文件, 则返回
                    converterFile(inputFile, outputPath , outputName);
                }
            }
        }
    }

    public static void converterFile(File inputFile, String outputPath,String outputFileName) throws OfficeException {
        File outputFile = new File(outputPath);
        if (!outputFile.exists()){
            outputFile.mkdir();
        }
        File outputFile2 = new File(outputPath + outputFileName);
        LocalConverter.Builder builder;
        builder = LocalConverter.builder();
        builder.build().convert(inputFile).to(outputFile2).execute();
    }
}
