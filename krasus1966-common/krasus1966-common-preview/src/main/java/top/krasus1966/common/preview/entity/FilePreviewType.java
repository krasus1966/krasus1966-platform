package top.krasus1966.common.preview.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/7 03:20
 **/
public class FilePreviewType {
    public static final String IMAGE = "image";
    public static final String PDF = "pdf";
    public static final String HTML = "html";

    /**
     * 文件后缀
     */
    private static final List<String> SUFFIX_WORD = Arrays.asList("docx", "wps", "doc", "docm", "rtf", "ppt", "pptx");
    private static final List<String> SUFFIX_EXCEL = Arrays.asList("xls", "xlsx", "csv", "xlsm", "xlt", "xltm", "et", "ett", "xlam");
    private static final List<String> SUFFIX_PDF = Collections.singletonList("pdf");
    private static final List<String> SUFFIX_IMAGE = Arrays.asList("jpg", "jpeg", "png", "gif", "bmp", "ico", "jfif", "webp");
    private static final List<String> SUFFIX_TEXT = Arrays.asList("txt","html","htm","asp","jsp","xml","json","properties","md","gitignore","log","java","py","c","cpp","sql","sh","bat","m","bas","prg","cmd");

    /**
     * 文件后缀对应预览方式
     */
    private static final List<String> PREVIEW_SUFFIX_PDF = Arrays.asList(PDF, IMAGE);
    private static final List<String> PREVIEW_SUFFIX_WORD = Arrays.asList(PDF, IMAGE);
    private static final List<String> PREVIEW_SUFFIX_EXCEL = Arrays.asList(IMAGE, HTML);
    private static final List<String> PREVIEW_SUFFIX_IMAGE = Arrays.asList(IMAGE);
    private static final List<String> PREVIEW_SUFFIX_TEXT = Arrays.asList(HTML);

    public enum Suffix {
        WORD(SUFFIX_WORD,PREVIEW_SUFFIX_WORD),
        EXCEL(SUFFIX_EXCEL,PREVIEW_SUFFIX_EXCEL),
        IMAGE(SUFFIX_IMAGE,PREVIEW_SUFFIX_IMAGE),
        TEXT(SUFFIX_TEXT,PREVIEW_SUFFIX_TEXT),
        PDF(SUFFIX_PDF,PREVIEW_SUFFIX_PDF),

        DEFAULT(Collections.emptyList(),PREVIEW_SUFFIX_PDF),

        ;

        private final List<String> suffixList;
        private final List<String> type;

        Suffix(List<String> suffixList,List<String> type) {
            this.suffixList = suffixList;
            this.type = type;
        }

        public List<String> getSuffixList() {
            return suffixList;
        }

        public List<String> getType(){
            return type;
        }

        public boolean checkType(String type) {
            return getType().contains(type);
        }
    }

    public static boolean checkType(String suffix,String type) {
        for (Suffix suffixEnum : Suffix.values()) {
            if (suffixEnum.getSuffixList().contains(suffix)) {
                return suffixEnum.checkType(type);
            }
        }
        return Suffix.DEFAULT.checkType(type);
    }


}
