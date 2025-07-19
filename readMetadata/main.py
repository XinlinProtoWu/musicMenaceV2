
import json
import tinytag
import chardet
import os

#Grab file path from json file
filePath = os.path.join(os.getcwd(), 'readMetadata', 'javaInput.json')
if __name__ == '__main__':
#set encoding to deal with any special characters
    with open(filePath, 'rb') as file:
        result = chardet.detect(file.read())
        encoding = result['encoding']
    with open(filePath, 'r', encoding=encoding) as file:
        #read metadata
        jsonData = json.load(file)
        path = jsonData.get('Path', '')
        audioFile = tinytag.TinyTag.get(path)
        title = audioFile.title
        artist = audioFile.artist
        duration = audioFile.duration
    data = {
        "title": title,
        "artist": artist,
        "duration": duration,
        "path": path
    }
    #dump metadata for java to read
    file_Path = os.path.join(os.getcwd(), 'pyOut.json')
    with open(file_Path, 'w') as file:
        jsonData = json.dumps(data)
        file.write(jsonData)