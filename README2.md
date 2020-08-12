

# m2 : 

## 触控模式：

channels: 

```json
[
  {
    "key": "6E:71:9C:8B:52:93",
    "name": "M2-L",
    "from": "zh-CN",
    "to": "en-US",
    "pronounce": 0,
    "tts": true,
    "recorder": "headset",
    "speaker": "headset",
    "role": "down"
  }
]

```

recorders: 

```json
[
  {
    "name": "headset",
    "speaker": "headset"
  }
]
```

## 听译模式

channels: 


```json
[
  {
    "key": "6E:71:9C:8B:52:93",
    "name": "M2-L",
    "from": "zh-CN",
    "to": "en-US",
    "pronounce": 0,
    "tts": true,
    "recorder": "mic",
    "speaker": "headset",
    "role": "down"
  }
]
```

recorders: 

```json
[
  {
    "name": "mic",
    "speaker": "headset"
  }
]
```
## 外放模式：

channels:

```json

[
  {
    "key": "down",
    "name": "M2-L",
    "from": "zh-CN",
    "to": "en-US",
    "pronounce": 0,
    "tts": true,
    "recorder": "headset",
    "speaker": 2,
    "role": "down"
  },
  {
    "key": "up",
    "name": "M2-L",
    "from": "en-US",
    "to": "zh-CN",
    "pronounce": 0,
    "tts": true,
    "recorder": "mic",
    "speaker": 3,
    "role": "up"
  }
]

```

recorders: 

```json
[
  {
    "name": "headset",
    "speaker": 2
  },
  {
    "name": "mic",
    "speaker": 3
  }
]
```


# wt2

## 同传模式(和触控模式一致)

channels:

```json
[
  {
    "key": "DA:91:D0:50:31:84",
    "name": "WT2-02",
    "from": "en-US",
    "to": "zh-CN",
    "pronounce": 0,
    "tts": true,
    "recorder": "bluetooth",
    "speaker": "bluetooth",
    "role": "up"
  },
  {
    "key": "ED:D9:88:CD:69:77",
    "name": "WT2-01",
    "from": "zh-CN",
    "to": "en-US",
    "pronounce": 0,
    "tts": true,
    "recorder": "bluetooth",
    "speaker": "bluetooth",
    "role": "down"
  }
]
```
recorders: 


```json
[
  {
    "name": "bluetooth",
    "speaker": "bluetooth"
  }
]
```

## 触控模式
channels:

```json
[
  {
    "key": "DA:91:D0:50:31:84",
    "name": "WT2-02",
    "from": "en-US",
    "to": "zh-CN",
    "pronounce": 0,
    "tts": true,
    "recorder": "bluetooth",
    "speaker": "bluetooth",
    "role": "up"
  },
  {
    "key": "ED:D9:88:CD:69:77",
    "name": "WT2-01",
    "from": "zh-CN",
    "to": "en-US",
    "pronounce": 0,
    "tts": true,
    "recorder": "bluetooth",
    "speaker": "bluetooth",
    "role": "down"
  }
]
```

recorders: 

```json
[
  {
    "name": "bluetooth",
    "speaker": "bluetooth"
  }
]
```

## 外放模式

channels:

```json
[
  {
    "key": "ED:D9:88:CD:69:77",
    "name": "WT2-01",
    "from": "zh-CN",
    "to": "en-US",
    "pronounce": 0,
    "tts": true,
    "recorder": "bluetooth",
    "speaker": "mic",
    "role": "down"
  },
  {
    "key": "up",
    "name": "up",
    "from": "en-US",
    "to": "zh-CN",
    "pronounce": 0,
    "tts": true,
    "recorder": "mic",
    "speaker": "bluetooth",
    "role": "up"
  }
]
```

recorders: 
```json
[
  {
    "name": "bluetooth",
    "speaker": "mic"
  },
  {
    "name": "mic",
    "speaker": "bluetooth"
  }
]
```



# zero





