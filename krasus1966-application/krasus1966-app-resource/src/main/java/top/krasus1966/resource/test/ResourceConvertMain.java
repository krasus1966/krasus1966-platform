package top.krasus1966.resource.test;

import lombok.extern.slf4j.Slf4j;
import ws.schild.jave.*;

import java.io.File;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * @author Krasus1966
 * @date 2023/5/3 23:24
 **/
@Slf4j
public class ResourceConvertMain {

    public static void main(String[] args) throws InterruptedException {
//        File file = compressionVideo(new File("/Users/krasus1966/Documents/01-Spring Cloud  Alibaba 微服务架构实战/从01到05/第02章 理解微服务架构，清楚微服务设计原则/2-1 微服务架构的演进过程.mp4"),
//                "/Users/krasus1966/Documents/01-Spring Cloud  Alibaba 微服务架构实战/从01到05/第02章 理解微服务架构，清楚微服务设计原则/", "test.mp4");
//        System.out.println(file.getPath());
        convert2mp4(new File("/Users/krasus1966/Documents/01-Spring Cloud  Alibaba 微服务架构实战/从01到05/第02章 理解微服务架构，清楚微服务设计原则/2-1 微服务架构的演进过程.mp4"), "/Users/krasus1966/Downloads/", "IMG_2172_libx264www.mp4");
//        cutFirstScreen(new File("/Users/krasus1966/Downloads/IMG_2172.MOV"), "/Users/krasus1966/Downloads/", "IMG_2172png.mp4");
//        cutFirstScreen(new File("/Users/krasus1966/Documents/01-Spring Cloud  Alibaba 微服务架构实战/从01到05/第02章 理解微服务架构，清楚微服务设计原则/2-1 微服务架构的演进过程.mp4"),
//                "/Users/krasus1966/Documents/01-Spring Cloud  Alibaba 微服务架构实战/从01到05/第02章 理解微服务架构，清楚微服务设计原则/", "test.mp4");
//        test();
    }

    public static void convert2mp4(File source, String targetFilePath, String targetFileName) {
        if (source == null) {
            return;
        }
        String newPath = targetFilePath + targetFileName;
        File target = new File(newPath);
        try {
            MultimediaObject object = new MultimediaObject(source);
            AudioAttributes audioAttributes = new AudioAttributes();
            VideoAttributes videoAttributes = new VideoAttributes();
            videoAttributes.setCodec("libx264");
            videoAttributes.setFaststart(true);
            EncodingAttributes attr = new EncodingAttributes();
            attr.setFormat("mp4");
            attr.setVideoAttributes(videoAttributes);
            attr.setAudioAttributes(audioAttributes);
            Encoder encoder = new Encoder();
            encoder.encode(object, target, attr);
            encoder.abortEncoding();
        } catch (Exception e) {
        }
    }

    public static void cutFirstScreen(File source, String targetFilePath, String targetFileName) {
        if (source == null) {
            return;
        }
        try {
            MultimediaObject object = new MultimediaObject(source);
            MultimediaInfo info = object.getInfo();
            VideoInfo videoInfo = info.getVideo();
            VideoSize videoSize = videoInfo.getSize();
            int height = videoSize.getHeight();
            int width = videoSize.getWidth();
            ScreenExtractor screenExtractor = new ScreenExtractor();
            screenExtractor.render(object, width,height, (int) (info.getDuration() * 0.001),
                    new File(targetFilePath), targetFileName, "png", 1);

        } catch (Exception e) {
        }
    }


    public static File test() {
//        if (source == null) {
//            return null;
//        }
//        String newPath = targetFilePath + targetFileName;
//        File target = new File(newPath);
        try {
            /*
            // 创建文件和ffmpeg对象
            MultimediaObject object = new MultimediaObject(source);

            // 使用ffmpeg获取文件信息
            MultimediaInfo info = object.getInfo();
            // 获取长度/秒
            long duration = info.getDuration();
            // 获取文件格式
            String format = info.getFormat();

            // 获取音频信息
            AudioInfo audioInfo = info.getAudio();
            // 音频流平均比特率，小于0则不可用
            int audioBitRate = audioInfo.getBitRate();
            // 音频通道数，1单声道，2双声道，小于0则不可用
            int audioChannels = audioInfo.getChannels();
            // 编码器名称
            String audioDecoder = audioInfo.getDecoder();
            // 音频采样率，小于0则不可用
            int audioSamplingRate = audioInfo.getSamplingRate();

            // 获取视频信息
            VideoInfo videoInfo = info.getVideo();
            // 视频流比特率，小于0则不可用
            int videoBitRate = videoInfo.getBitRate();
            // 编码器名称
            String videoDecoder = videoInfo.getDecoder();
            // 视频大小，返回null则不可用
            VideoSize videoSize = videoInfo.getSize();
            // 视频高度
            int height = videoSize.getHeight();
            // 视频宽度
            int width = videoSize.getWidth();
            // 视频帧速度，小于0则不可用
            float videoFrameRate = videoInfo.getFrameRate();


            // 创建音频属性
            AudioAttributes audioAttributes = new AudioAttributes();
            // 编解码器名称
            audioAttributes.setCodec();
            // 比特率值
            audioAttributes.setBitRate();
            // 声道
            audioAttributes.setChannels();
            // 音频质量
            audioAttributes.setQuality();
            // 采样率值
            audioAttributes.setSamplingRate();
            // 音量值
            audioAttributes.setVolume();


            // 创建视频属性
            VideoAttributes videoAttributes = new VideoAttributes();
            // 编解码器名称
            videoAttributes.setCodec();
            // 比特率值
            videoAttributes.setBitRate();
            // 视频大小
            videoAttributes.setSize();
            // 视频质量
            videoAttributes.setQuality();
            // 帧速率值
            videoAttributes.setFrameRate();
            // 快速入门设置
            videoAttributes.setFaststart();
            // 强制标记
            videoAttributes.setTag();
            // x264配置
            videoAttributes.setX264Profile();

            // 创建编码属性
            EncodingAttributes attr = new EncodingAttributes();
            // 设置格式
            attr.setFormat("mp4");
            // 设置音频属性
            attr.setAudioAttributes(audioAttributes);
            // 设置视频属性
            attr.setVideoAttributes(videoAttributes);
            // 开始偏移时间/秒
            attr.setOffset();
            // 重新编码持续时间/秒
            attr.setDuration();
            // 如果可能，将元数据从原始文件复制到新输出
            attr.setMapMetaData();

            // 截屏
            ScreenExtractor screenExtractor = new ScreenExtractor();
            // 找到的屏幕数
            int numberOfScreens = screenExtractor.getNumberOfScreens();
            // 从源视频生成屏幕截图。
            // 参数： multimediaObject – 源多媒体对象@see多媒体对象
            //       width – 输出宽度
            //       height – 输出高度
            //       seconds – 屏幕之间的间隔（以秒为单位）
            //       outputDir –输出图像的目的地
            //       fileNamePrefix - 所有缩略图的名称
            //       extension – 输出的图像扩展（jpg、png 等）
            //       quality – 范围在 1-31 之间，其中 31 是质量最差的
            screenExtractor.render();*/

            // 创建编码器
            Encoder encoder = new Encoder();
            // 支持的音频转码器
            String[] audioEncoders = encoder.getAudioEncoders();
//            System.out.println("音频转码器：");
//            Arrays.stream(audioEncoders).forEach(System.out::println);
            // 支持的音频解码器
            String[] audioDecoders = encoder.getAudioDecoders();
//            System.out.println("音频解码器：");
//            Arrays.stream(audioDecoders).forEach(System.out::println);
            // 支持的视频转码器
            String[] videoEncoders = encoder.getVideoEncoders();
            System.out.println("视频转码器：");
            Arrays.stream(videoEncoders).forEach(System.out::println);
            // 支持的视频解码器
            String[] videoDecoders = encoder.getVideoDecoders();
//            System.out.println("视频解码器：");
//            Arrays.stream(videoDecoders).forEach(System.out::println);
            // 支持的文件转码格式
            String[] supportedEncodingFormats = encoder.getSupportedEncodingFormats();
//            System.out.println("文件转码格式：");
//            Arrays.stream(supportedEncodingFormats).forEach(System.out::println);
            // 支持的文件解码格式
            String[] supportedDecodingFormats = encoder.getSupportedDecodingFormats();
//            System.out.println("文件解码格式：");
//            Arrays.stream(supportedDecodingFormats).forEach(System.out::println);
            // ffmpeg未处理的输出消息列表
            List<String> unhandledMessages = encoder.getUnhandledMessages();
            // 开始转码
//            encoder.encode(object, target, attr);
        } catch (Exception e) {

        }
        return null;
    }

    public static File compressionVideo(File source, String targetFilePath, String targetFileName) {
        if (source == null) {
            return null;
        }
        String newPath = targetFilePath + targetFileName;
        File target = new File(newPath);
        try {
            MultimediaObject object = new MultimediaObject(source);
            AudioInfo audioInfo = object.getInfo().getAudio();
            // 根据视频大小来判断是否需要进行压缩,
            int maxSize = 50;
            double mb = Math.ceil(source.length() / 1048576);
            int second = (int) object.getInfo().getDuration() / 1000;
            BigDecimal bd = new BigDecimal(String.format("%.4f", mb / second));
            log.info("开始压缩视频了--> 视频每秒平均 " + bd + " MB ");
            // 视频 > 50MB, 或者每秒 > 0.5 MB 才做压缩， 不需要的话可以把判断去掉
            boolean temp = mb > maxSize || bd.compareTo(new BigDecimal("0.5")) > 0;
            if (temp) {
                long time = System.currentTimeMillis();
                //TODO 视频属性设置
                int maxBitRate = 128000;
                int maxSamplingRate = 44100;
                int bitRate = 800000;
                int maxFrameRate = 20;
                int maxWidth = 1280;

                AudioAttributes audio = new AudioAttributes();
                // 设置通用编码格式10
//                audio.setCodec("aac");
                // 设置最大值：比特率越高，清晰度/音质越好
                // 设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 128000 = 182kb)
                if (audioInfo.getBitRate() > maxBitRate) {
                    audio.setBitRate(maxBitRate);
                }

                // 设置重新编码的音频流中使用的声道数（1 =单声道，2 = 双声道（立体声））。如果未设置任何声道值，则编码器将选择默认值 0。
                audio.setChannels(audioInfo.getChannels());
                // 采样率越高声音的还原度越好，文件越大
                // 设置音频采样率，单位：赫兹 hz
                // 设置编码时候的音量值，未设置为0,如果256，则音量值不会改变
                // audio.setVolume(256);
                if (audioInfo.getSamplingRate() > maxSamplingRate) {
                    audio.setSamplingRate(maxSamplingRate);
                }

                //TODO 视频编码属性配置
                VideoInfo videoInfo = object.getInfo().getVideo();
                VideoAttributes video = new VideoAttributes();
                video.setCodec("h264");
                //设置音频比特率,单位:b (比特率越高，清晰度/音质越好，当然文件也就越大 800000 = 800kb)
                if (videoInfo.getBitRate() > bitRate) {
                    video.setBitRate(bitRate);
                }

                // 视频帧率：15 f / s  帧率越低，效果越差
                // 设置视频帧率（帧率越低，视频会出现断层，越高让人感觉越连续），视频帧率（Frame rate）是用于测量显示帧数的量度。所谓的测量单位为每秒显示帧数(Frames per Second，简：FPS）或“赫兹”（Hz）。
                if (videoInfo.getFrameRate() > maxFrameRate) {
                    video.setFrameRate(maxFrameRate);
                }

                // 限制视频宽高
                int width = videoInfo.getSize().getWidth();
                int height = videoInfo.getSize().getHeight();
                if (width > maxWidth) {
                    float rat = (float) width / maxWidth;
                    video.setSize(new VideoSize(maxWidth, (int) (height / rat)));
                }

                EncodingAttributes attr = new EncodingAttributes();
                attr.setFormat("mp4");
                attr.setAudioAttributes(audio);
                attr.setVideoAttributes(video);

                Encoder encoder = new Encoder();
                encoder.encode(new MultimediaObject(source), target, attr);
                log.info("压缩总耗时：" + (System.currentTimeMillis() - time) / 1000);
                return target;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //删除源文件
            if (target.length() > 0) {
//                source.delete();
            }
        }
        return source;
    }

}