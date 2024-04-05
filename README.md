This project was for hackathon, which was organized by [NeoBook]([url](https://neobook.club/))
The task was to build a front and backend to register users by email. 
By this project, you can register users by sending Gmail messages. However, there are several changes that you should make.

![image](https://github.com/AbrorbekAllaberganov/PetHack/assets/83587031/5fb25995-cd21-4b43-905a-75aa8f53bcbd)

First of all, in the application.proporties file, you have to enter an email that sends gmail messages and a password to get access to this email.

There is one problem that I couldn't solve (maybe I will find an answer in the future).
In the com.example.PetHack.service.impl, you can see sendCodeToEmail method. There is a builder template for email message. I wanted to use html file, but in that case I couldn't find a way to put an email message in the html code, so I used a string. 
If you have an answer for this problem or any question, you can ask it in the comment section.
