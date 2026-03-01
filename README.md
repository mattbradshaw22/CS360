# CS360

Briefly summarize the requirements and goals of the app you developed. What user needs was this app designed to address?

The goal of this app was to implement an SQLite database stored on the device and allow the user to have a clean way to interact with the database to update quantities of items and add or remove them quickly. It required secure login practices and allowing sms permissions to alert the user of out of stock items. This addresses the user needs of digitizing the inventory and being able to track and change it  from a mobile device to make work tasks more efficient.

What screens and features were necessary to support user needs and produce a user-centered UI for the app? How did your UI designs keep users in mind? Why were your designs successful?

To support user needs there was a login screen to enable secure login practices tying user credentials to a database and the main screen once logining in used a recycler view to display the data from the inventory database. I implemented features including various buttons and a floating action button for the user to interact with the data in the database that allows them to update add or remove items from the database quickly and cleanly. To keep the user in mind I made sure that the buttons were easily accessible for the user when using a device following material and layout design principles standard to android development.

How did you approach the process of coding your app? What techniques or strategies did you use? How could those techniques or strategies be applied in the future?

In coding this app I broke the overall project down into smaller tasks, first creating the layouts then developing code to enable the user login making sure navigation went to the main page when a user was logged in. Next I worked on the code to enable the recycler view to display info from the database before working on the code for the buttons that would interact with the data from the database and change the display in the recycler view. One of the final steps was implementing the SMS feature that checks for out of stock items in the database and sends an SMS alert to the user if the item is out of stock and they have SMS permissions enabled.

How did you test to ensure your code was functional? Why is this process important, and what did it reveal?

I mainly tested by running the app in the android emulator and verifying values while debugging the app. I  found that for such a simple task it was easiest to test it in the emulator by inputiing data manually and verifying the results. The testing process is important to verify that things work as intended to not ship an unfunction version of the app to customers. It revealed several buttons I hadn't got to function properly and sms alerts that were not working before I was able to properly fix them.

Consider the full app design and development process from initial planning to finalization. Where did you have to innovate to overcome a challenge?

For me the biggest challenge was thinking about user needs, thinking about how someone would want an app to work for a task ive never had to do is challenging and its hard to be sure that what I developed is going to be right or work for them without any feedback. However I came up with a design and implemented it and theoretically could get user input on it now and go back to the drawing board if needed. My biggest challenge was implementing the user login, I tried to use the built in login fragment and was a bit overwhelmed with it at first, but after watching a few youtube videos on it I got a grasp of it and think I made it work good enough for the project. 

In what specific component of your mobile app were you particularly successful in demonstrating your knowledge, skills, and experience?

I was most succesful in troubleshooting and reworking my code to get everything functioning and running smoothly, demonstating my good understanding of working with databases and creating user friendly ways to interact with them.
