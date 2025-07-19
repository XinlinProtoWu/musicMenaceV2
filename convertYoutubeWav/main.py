# This is a sample Python script.

# Press Shift+F6 to execute it or replace it with your code.

import ffmpeg
import yt_dlp
import sys
import json
import os
def convertMusic(url):
    ydl_opts = {
        'format': 'bestaudio/best',
        'postprocessors': [{
            'key': 'FFmpegExtractAudio',
            'preferredcodec': 'wav',
        }],
    }
    #download
    def download_from_url(url):
        ydl.download([url])
        input_file = 'output.m4a'
        output_file_wav = 'output.wav'
        stream = ffmpeg.input(input_file)
        stream = ffmpeg.output(stream, output_file_wav)

    #extract and format
    with yt_dlp.YoutubeDL(ydl_opts) as ydl:
        global musicPath
        args = sys.argv[1:]
        info_dict = ydl.extract_info(url, download=False)
        video_title = info_dict.get('title', None)
        video_id = info_dict.get('id', None)
        musicPath = video_title + ' [' + video_id + '].wav'
        if len(args) > 1:
            print("Too many arguments.")
            print("Usage: python youtubetowav.py <optional link>")
            print("If a link is given it will automatically convert it to .wav. Otherwise a prompt will be shown")
            exit()
        if len(args) == 0:
            download_from_url(url)
        else:
            download_from_url(args[0])
if __name__ == '__main__':
    #read url
    filePath = os.path.join(os.getcwd(), 'convertYoutubeWav', 'javaInp.json')
    with open(filePath, 'r') as file:
        jsonData = json.load(file)
        url = jsonData.get('url', '')
    convertMusic(url)
    data = {
        "Path": musicPath
    }
    #write created file path
    file_Path = os.path.join(os.getcwd(), "convertPath.json")
    with open(file_Path, 'w') as file:
        jsonData = json.dumps(data)
        file.write(jsonData)