---
layout: home
permalink: index.html

# Please update this with your repository name and title
repository-name: e19-co227-voice-based-attendance-management-system
title:
---
[comment]: # "This is the standard layout for the project, but you can clean this and use your own template"

# Voice-Based Attendance Management System


<!-- 
This is a sample image, to show how to add images to your page. To learn more options, please refer [this](https://projects.ce.pdn.ac.lk/docs/faq/how-to-add-an-image/)

![Sample Image](./images/sample.png)
 -->
 ---
## Our Team
-  E/19/142, Illangarathne Y.M.H.V., [email](mailto:e19142@eng.pdn.ac.lk)
-  E/19/363, Senevirathna T.N., [email](mailto:e19363@eng.pdn.ac.lk)
-  E/19/375, Silva N.H.D.U., [email](mailto:e19375@eng.pdn.ac.lk)
-  E/19/505, Witharana S.P.M., [email](mailto:e19505@eng.pdn.ac.lk)
  
---

## Table of Contents
1. [Problem](#Problem)
2. [Solution](#Solution)
3. [Technology Stack](#TechnologyStack)
4. [Links](#links)

---

## Problem

Calculating the attendance in the paper-based system is difficult and can be erroneous. Also, Student can sign for others and fake their attendance. This will lead to bad analytics and problems in attendance. Another problem is attendance sheets can be lost. If one attendance sheet gets lost it will make a significant difference in the attendance analytics. Also, students lose focus on the lecture while passing the attendance sheet. One major problem is taking attendance in online meetings as online meetings have become a day-to-day thing in the world now. If the meeting has a huge amount of participants, taking attendance manually will take a huge amount of time and it may be erroneous.

<br> <br>

## Solution

For this problem, there can be multiple solutions. Such as fingerprint, image-based attendance management systems, voice-based attendance management systems. However when considering online classes, using fingerprint is not suitable. Image-based attendance management systems can be faulty due to lighting conditions(night classes) and the absence of a good camera. So, the best solution for the problem is using a voice-based attendance management system.

<br> <br>

## Technology Stack
<br>
+ Java : Used for front-end logic, and database connectivity.<br><br>
<p align="center">
  <img height="200" src="https://upload.wikimedia.org/wikipedia/en/3/30/Java_programming_language_logo.svg">
</p>
<br>
<br>

+ JavaFX : Used for GUI design.<br><br>
<p align="center">
  <img width = "250" height="200" src="https://upload.wikimedia.org/wikipedia/en/c/cc/JavaFX_Logo.png">
</p>
<br>
<br>

+ Python : Used for audio analyzing.<br><br>
<p align="center">
  <img height="200" src="https://upload.wikimedia.org/wikipedia/commons/thumb/c/c3/Python-logo-notext.svg/1200px-Python-logo-notext.svg.png">
</p>
<br>
<br>

+ VOSK : Vosk is a speech recognition toolkit. Vosk can be used offline and it's free. <br><br>
<p align="center">
  <img height="200" src="https://www.openhab.org/logos/voskstt.png">
</p>
<br>
<br>

+ SQLite : Database management system. <br><br>
<p align="center">
  <img height="200" src="https://download.logo.wine/logo/SQLite/SQLite-Logo.wine.png">
</p>
<br>

## GUI And Funtionality

+ First the user need to register to the system by giving their User ID which they will use when they are speaking to mark the attendance. These are the GUI that user will use,

![Registration](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/7c2d71a6-1fc3-4452-a1f7-67c6c59fa586)

+ When they successfully register, They will get a screen like this saying registration success.

![RegistrationSuccessful](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/75bd82e6-792d-4b85-80a7-1dd033fd6a52)

+ Registration only need to be done one time only. Then the user can mark their attendace. User can press the record button in this screen and start recroding.
  
![MarkAttendanceHome](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/10182477-3215-4004-abbe-9a9fd537b25f)

![RecordingScreen](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/359e7da9-9981-492d-a16f-e0e61b4a193d)

+ If the system couldn't transcribe the sound due to some noise or hardware error, then the user can enter their registration number. But the voice recording will be used to comparison. So there should not be a big noise.

![TranscribingFail](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/6da7b463-ca77-49c6-8b5b-6132035a4d6e)

+ If the recording is transcribed successfully, then this screen will be prompted.

![RecordingSuccess](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/c5c9977d-101b-4525-aab3-da0b920bb488)

+ Finally, the user will get a confirmation screen with their User ID and name.

![RecordingInfo](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/1dc4381c-8b8f-48c1-93ca-888769d0c275)

+ Finally, the voice will be comapred and It will be displayed if the voice matches or not.

![VoiceMatch](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/ea967ca1-8385-488e-b750-18ba61c509ae)

![VoiceDontMatch](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/e674b553-5cd4-496e-bb79-8ecd3fbd8e24)

+ That is the funtionality of the application for the user. Then the admins can log in to the system using this screen and Enter the information they need and get users attendance information.
  
![Login](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/3f4816d1-5b21-4997-a76d-3db1626769d2)

![AdminPanel](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/e3d06212-d30b-45e6-bdd0-0b5cbfa23e83)

![UserInfo](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/5c6ccddc-3219-4848-a827-ee1ceba4715a)

![UserInfoDisplay](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system/assets/115541700/fa771808-ca0e-4b4c-b6b1-3c30c9ddd571)


## Project TimeLine

- [x] Week 1: Planning and Requirements Gathering 
- [x] Week 2: Design and coming up with a solution
- [x] Week 3: Development of UX/UI, Backend  and Database Connectivity
- [x] Week 4: Finishing, Testing, Debugging and Quality Assurance
- [x] Week 5: Deployment, Launch, and User Training
<br> <br>

## Links

- [Project Repository](https://github.com/cepdnaclk/e19-co227-voice-based-attendance-management-system)
- [Project Page](https://cepdnaclk.github.io/e19-co227-voice-based-attendance-management-system/)
- [Department of Computer Engineering](http://www.ce.pdn.ac.lk/)
- [University of Peradeniya](https://eng.pdn.ac.lk/)


[//]: # (Please refer this to learn more about Markdown syntax)
[//]: # (https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)
