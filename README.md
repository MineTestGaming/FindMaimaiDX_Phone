# FindMaimaiDX

## 简介

`FindMaimaiDX` 是一款专为舞萌（Maimai）爱好者设计的Android应用程序，旨在帮助用户快速找到附近的舞萌机厅。通过本应用，用户不仅可以获取当前位置附近的机厅信息，还可以手动搜索特定地区的机厅。
欢迎加入 `669030069` qq群一起维护这个项目！
## 特性

- **自动定位**：开启权限后，应用将自动获取用户的当前位置，并显示附近的所有舞萌机厅。
- **手动搜索**：用户可以通过输入具体地址来查找目标区域内的舞萌机厅。
- **联系开发者**：内置了与开发者直接沟通的功能，方便用户反馈问题或提出建议。
- **地图导航**：提供机厅的具体位置信息，支持用户一键导航至目的地。

## 使用方法

### 安装

1. 下载最新版本的 `FindMaimaiDX.apk` 文件。
2. 打开下载的文件，按照提示安装应用。

### 开始使用

1. **授权位置权限**：首次打开应用时，需要授权应用访问您的位置信息。
2. **自动刷新定位**：点击应用首页的刷新按钮，应用将自动获取并显示当前位置附近的舞萌机厅列表。
3. **手动选择定位**：若想查找其他地区的机厅，可以选择手动输入目标地址进行搜索。
4. **联系作者**：在应用内选择“联系作者”选项，即可通过预设的邮件模板向开发者发送反馈或建议。

## 技术栈

- **编程语言**：Java
- **框架**：Android SDK
- **网络请求**：OkHttp
- **数据解析**：Gson
- **UI组件**：RecyclerView, AlertDialog

## 项目结构

- `MainActivity.java`：主活动，负责处理用户交互、获取位置信息及显示机厅列表。
- `Place.java`：定义了机厅的信息模型。
- `PlaceAdapter.java`：用于RecyclerView的适配器，展示机厅列表。
- `DistanceCalculator.java`：计算两个地理位置之间的距离。
- `AddressParser.java`：解析地址信息。

## 贡献

欢迎任何对项目感兴趣的开发者贡献代码或提出宝贵的建议。您可以直接在GitHub上创建Issue或Pull Request。

## 许可证

本项目遵循MIT许可证，详情请参阅LICENSE文件。

## 技术致谢
感谢以下人员对我的技术帮助:
- Bainuo
- silvaryo
- TDYJRS
## 法务致谢
感谢以下人员对我的法律责任方面的帮助:
- 坠星
## 发电致谢
感谢以下人员对我的直接支持:
- OCL
- 小孩哥
- 🐾zs.星炘🐾 ⁧!喵⁧
