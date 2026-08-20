# JAVE2 examples

Recipes for the conversions people ask for most. See [Usage](https://github.com/a-schild/jave2/wiki/Usage) for how to add the
library, and [Supported formats](https://github.com/a-schild/jave2/wiki/Supported-formats) for what the bundled ffmpeg can read
and write.

> Every example calls `attrs.setOutputFormat(...)`. Older examples elsewhere on the
> internet call `attrs.setFormat(...)`, which has not existed for a long time and will
> not compile.

## Audio

### WAV to MP3

``` JAVA
File source = new File("source.wav");
File target = new File("target.mp3");

AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setBitRate(128000);
audio.setChannels(2);
audio.setSamplingRate(44100);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp3");
attrs.setAudioAttributes(audio);

new Encoder().encode(new MultimediaObject(source), target, attrs);
```

### Extracting the audio from a video, as WAV

``` JAVA
File source = new File("source.avi");
File target = new File("target.wav");

AudioAttributes audio = new AudioAttributes();
audio.setCodec("pcm_s16le");

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("wav");
attrs.setAudioAttributes(audio);

new Encoder().encode(new MultimediaObject(source), target, attrs);
```

### Taking the soundtrack off a video, as MP3

Leaving `VideoAttributes` unset is what drops the picture.

``` JAVA
File source = new File("source.mp4");
File target = new File("target.mp3");

AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setBitRate(192000);
audio.setChannels(2);
audio.setSamplingRate(44100);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp3");
attrs.setAudioAttributes(audio);

new Encoder().encode(new MultimediaObject(source), target, attrs);
```

If the source audio is already MP3, `audio.setCodec(AudioAttributes.DIRECT_STREAM_COPY)`
lifts the stream out unchanged, which is both faster and lossless.

### FLAC to MP4, audio only

An MP4 carrying nothing but sound. Same shape as above, a different container, and
`setFaststart` so it can be streamed.

``` JAVA
AudioAttributes audio = new AudioAttributes();
audio.setCodec("aac");
audio.setBitRate(256000);

VideoAttributes video = new VideoAttributes();
video.setFaststart(true);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp4");
attrs.setAudioAttributes(audio);
attrs.setVideoAttributes(video);
```

Players that insist on a picture want a video stream, so for those, loop a still image
instead. See [Encoding Attributes](https://github.com/a-schild/jave2/wiki/Encoding-Attributes) for `setLoop`.

### FLAC to MP3, keeping the album art

Cover art is carried as a video stream, so an audio only encoding drops it. Ask for the
video stream and ask for it unchanged.

``` JAVA
AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setBitRate(320000);

VideoAttributes video = new VideoAttributes();
video.setCodec("copy");          // this is what keeps the cover

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp3");
attrs.setAudioAttributes(audio);
attrs.setVideoAttributes(video);

new Encoder().encode(new MultimediaObject(source), target, attrs);
```

You can check it survived, since the cover shows up as a video stream:

``` JAVA
boolean hasCover = new MultimediaObject(target).getInfo().getVideo() != null;
```

### To AMR, for telephony

``` JAVA
AudioAttributes audio = new AudioAttributes();
audio.setCodec("libopencore_amrnb");
audio.setSamplingRate(8000);     // amr-nb only does 8 kHz
audio.setChannels(1);            // and mono
audio.setBitRate(12200);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("amr");
attrs.setAudioAttributes(audio);
```

### Changing the volume

``` JAVA
AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setVolume(512);            // 256 is unchanged, so this is twice as loud
```

## Video

### AVI to FLV

``` JAVA
File source = new File("source.avi");
File target = new File("target.flv");

AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setBitRate(64000);
audio.setChannels(1);
audio.setSamplingRate(22050);

VideoAttributes video = new VideoAttributes();
video.setCodec("flv");
video.setBitRate(160000);
video.setFrameRate(15);
video.setSize(new VideoSize(400, 300));

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("flv");
attrs.setAudioAttributes(audio);
attrs.setVideoAttributes(video);

new Encoder().encode(new MultimediaObject(source), target, attrs);
```

### To MP4, H.264

``` JAVA
AudioAttributes audio = new AudioAttributes();
audio.setCodec("aac");
audio.setBitRate(128000);

VideoAttributes video = new VideoAttributes();
video.setCodec("libx264");
video.setCrf(23);                // 0 lossless, 51 worst, 23 is the usual default
video.setPreset(PresetEnum.MEDIUM.getPresetName());   // or just "medium"

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp4");
attrs.setAudioAttributes(audio);
attrs.setVideoAttributes(video);
```

### To WebM, VP9

``` JAVA
AudioAttributes audio = new AudioAttributes();
audio.setCodec("libopus");
audio.setBitRate(96000);

VideoAttributes video = new VideoAttributes();
video.setCodec("libvpx-vp9");
video.setBitRate(250000);
video.setFrameRate(25);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("webm");
attrs.setAudioAttributes(audio);
attrs.setVideoAttributes(video);
```

### Taking a portion out of the middle

``` JAVA
EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp4");
attrs.setOffset(30.0f);          // start thirty seconds in
attrs.setDuration(10.0f);        // and take ten seconds
```

### Videos filmed sideways

Phones record the orientation as metadata rather than rotating the picture, so a video
that looks upright in a phone gallery can arrive on its side.

``` JAVA
MultimediaInfo info = new MultimediaObject(source).getInfo();
if (info.getRotate() == 90 || info.getRotate() == 270) {
    // width and height of info.getVideo().getSize() are the other way round
    // once it is played back
}
```

## Several files at once

### Joining videos

``` JAVA
List<MultimediaObject> sources = new ArrayList<>();
sources.add(new MultimediaObject(new File("part1.mp4")));
sources.add(new MultimediaObject(new File("part2.mp4")));

VideoAttributes video = new VideoAttributes();
video.setCodec("mpeg4");

FilterGraph graph = new FilterGraph();
FilterChain chain = new FilterChain();
chain.addFilter(new MediaConcatFilter(sources.size(), true, true));  // video, audio
graph.addChain(chain);
video.setComplexFiltergraph(graph);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp4");
attrs.setVideoAttributes(video);

new Encoder().encode(sources, target, attrs);
```

> When several sources are joined there is no single source to describe, so
> `EncoderProgressListener.sourceInfo` is not called, and progress is reported as
> `PROGRESS_UNKNOWN` rather than a percentage.

### Joining audio files

The same filter, told to take the audio streams and not the video ones. Note that the
filter graph still hangs off `VideoAttributes` even though the output has no video, since
that is where `setComplexFiltergraph` lives.

``` JAVA
List<MultimediaObject> sources = new ArrayList<>();
sources.add(new MultimediaObject(new File("1.mp3")));
sources.add(new MultimediaObject(new File("2.mp3")));

AudioAttributes audio = new AudioAttributes();
audio.setCodec("libmp3lame");
audio.setChannels(2);
audio.setSamplingRate(44100);

FilterGraph graph = new FilterGraph();
FilterChain chain = new FilterChain();
chain.addFilter(new MediaConcatFilter(sources.size(), false, true));   // no video, audio
graph.addChain(chain);

VideoAttributes video = new VideoAttributes();
video.setComplexFiltergraph(graph);

EncodingAttributes attrs = new EncodingAttributes();
attrs.setOutputFormat("mp3");
attrs.setAudioAttributes(audio);
attrs.setVideoAttributes(video);

new Encoder().encode(sources, target, attrs);
```

The two booleans are which streams to carry: `MediaConcatFilter(n, video, audio)`. Passing
`(n, true, false)` joins the pictures and drops the sound, and the single argument
constructor takes both, which fails if any source lacks either.

> All the sources must have compatible streams. Files with different sample rates or
> channel counts need converting first, one at a time, and joining afterwards.

## Stills

### One frame

``` JAVA
ScreenExtractor extractor = new ScreenExtractor();
extractor.renderOneImage(new MultimediaObject(source), 320, 240, 2000L, target, 2);
//                                                     w    h   millis  file quality
```

### A frame every so many seconds

``` JAVA
extractor.render(new MultimediaObject(source), 320, 240, 5,
                 outputDirectory, "frame", "jpg", 2);
//                                          every 5 seconds, into a directory
```

## Filters

``` JAVA
VideoAttributes video = new VideoAttributes();

// text across the picture
video.addFilter(new DrawtextFilter("watermark", "10", "10", "Sans", 24.0,
                                   new Color("ffffff")));

// or scale it
video.addFilter(new ScaleFilter(new VideoSize(1280, 720)));

// or crop it
video.addFilter(new CropFilter(640, 480, 0, 0));   // width, height, x, y
```

Burning in subtitles takes the file itself, the filter escapes the path for you:

``` JAVA
video.addFilter(new AssSubtitlesFilter(new File("subtitles.ass")));
```

## Following along

``` JAVA
new Encoder().encode(new MultimediaObject(source), target, attrs,
    new EncoderProgressListener() {
        public void sourceInfo(MultimediaInfo info) { }
        public void progress(int permil) {
            if (permil == EncoderProgressListener.PROGRESS_UNKNOWN) {
                System.out.println("working");
            } else {
                System.out.println(permil / 10.0 + "%");
            }
        }
        public void message(String message) { }
        @Override public void done() {
            System.out.println("finished");
        }
    });
```

`PROGRESS_UNKNOWN`, which is `-1`, means the source declared no duration, so there is
nothing for the progress to be a proportion of. Live streams, webm files from browser
recorders and concatenations all land there, and an indeterminate progress bar is the
right response.

## Stopping a running encoding

`encode()` blocks, so to be able to abort it, run it on a thread of your own and keep the
`Encoder` where the other thread can reach it. `abortEncoding()` kills the ffmpeg process.

``` JAVA
Encoder encoder = new Encoder();

Thread thread = new Thread(() -> {
    try {
        encoder.encode(new MultimediaObject(source), target, attrs, listener);
        // finished
    } catch (EncoderException ex) {
        // failed, or was aborted
    }
});
thread.start();

// later, from anywhere
encoder.abortEncoding();
```

`quitEncoding()` is the gentler alternative: it sends ffmpeg a `q`, so ffmpeg finishes
writing what it has and closes the file properly, leaving a shorter but playable result.
`abortEncoding()` kills the process outright and the partial file is usually unusable.
Neither is any use once the run has ended, and `quitEncoding()` throws if nothing is
running.

One `Encoder` runs one encoding at a time, since it keeps a single process, so give each
concurrent conversion its own.

## Something not covered here

If the option you need is not in the typed API, see
[Custom ffmpeg arguments](https://github.com/a-schild/jave2/wiki/Custom-ffmpeg-arguments).

The test sources in
[`jave-core-test`](https://github.com/a-schild/jave2/tree/master/jave-core-test/src/test/java/ws/schild/jave)
exercise most of the library against real files, and are worth reading when you want to see
a class used in anger.
