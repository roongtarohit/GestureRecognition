# GestureRecognition

ABSTRACT: - Book recommendation applications are a boon for booklovers. Different kind of book recommendation applications are available but there is a lot of scope for improving user accessibility. In this project we are developing a similar application that can ease a user’s access through image recognition features. Users can quickly click a picture of the book and get recommendations based on it. A brief report based on how the application works has been presented here.

Author Keywords: - OCR, Google Mobile Services, Google API, Recommendation

INTRODUCTION
With the advent of technology, the lives of people have become fast paced and their dependencies on cell phones have increased significantly. Image recognition can reportedly improve user’s accessibility since they can easily access anything by simply clicking a picture of it. We have proposed and developed a book recommendation application to make lives of booklovers easier. Our project implements 20 different tasks in one application. We have shown a clear representation of each task and its flow in this report. The implementation has been done using android studio and Python.

TASK BREAKUP AND IMPLEMENTATION

Task 1-3: -User Login, Register and Validation
A user can register from the registration page. Username and password are required for the registration process.
The user can then login to the application using the username and password used during the registration process.
Checks has been implemented to check for passwords and username. Only a registered user can login to the app otherwise a pop up will be shown with message as “Invalid Credentials”.
 
Task 4-6: -Search by User Input using Author, Search by using ISBN, Search by using Book name
Three different options are provided to the user after login. The user can search a book by any one of the fields which in turn fetches the data by using Google API.

Task 7-10: -Search by Barcode, Click or Choose Image from Gallery, Crop Image for further processing, Barcode Validation
On clicking search by barcode, user is prompted to either click an image or choose an image from Gallery.
The image is cropped accordingly to scan the barcode properly. Google Mobile Vision API has been used for scanning barcode, which is further used for searching the book. Validation check for a proper barcode is implemented. The title of the book is then processed for search.

Task 11: - Search using Camera (OCR), Crop Image for further processing
Computer Vision Library (OpenCV) and Python Imaging Library are used to load the image and process it. The title is segregated from the content by using the following steps:
1) Image conversion is done from RGB to Binary.
2) Contour Height Heuristic is applied 3)RLSA algorithm is applied to connect title contours and do noise cancellation. Run Length Smoothing Algorithm discriminates text and is used for block segmentation.
4)Further the text is filtered using width heuristic.
 
Task 12: - Search Result Page
On hitting search button, the result shows Book details, rating and price based on Google API. In the side tabs books recommended based on books by same author and similar books are also shown.

Task 13: - Backend Server
The backend server is responsible for handling the user registration and password validation. When the user register on the app the backend server takes and store the username and password in database which is later used for validation purpose. The backend server also handles the authentication for google API and cleaning of data retrieved from google books API.

Task 14: - Recommendation based on same author and similar books
For book recommendation we are using google API books recommendation. When we search for a book by providing author name or book title, we get recommendation such as more by same author or more books of similar category. These recommendations are provided by google API itself along with other details such as price of the book, reviews and synopsis.

Task 15: - UX Design
Application Logo has been designed

Task 16,17: -Book Details and Synopsis
The user can search a book in the application, on successful hit book details and ratings are shown. He/she can click on a particular book to view more details. A new page opens up showing the description of the book, ASU link, INFO link and book title. On clicking the link, the user will be redirected to ASU Library page.

Task 18: - Buy a book
Algorithm Implementation:-On successful search, the user can click on a book to view more details. A new window with the link ‘INFO link’ opens. On clicking the INFO link, the application redirects the user to google buy page.

Task 19: -Redirect to ASU Lib
On searching a particular book, the user will be redirected to a page showing results. If a book is selected, it will redirect to the description page having ASU Link.

Task 20: - Test the functionality
The user can register by using his credentials and if he/she provides wrong details in the login page they are prompted with the reply “Invalid Credentials”. Validation checks works properly. Upon successful entry, users can search a book manually by author, ISBN, book title and get forwarded to the search page. Search functionality works fine too. Users are able to click a picture of the book and crop the same, thus the application ensures that the camera sensors are working fine. The title of the book is properly fetched from the image clicked. Upon searching the same appropriate search results are shown. Users can click a picture and scan the barcode, if the picture doesn’t resemble a proper barcode it prompts ‘Invalid barcode’, thereby ensuring that the barcode check validation works fine. The search results of the barcode scanner work perfectly. On selection of a particular book more details are shown. The info link redirects the user to Google buy page and ASU link redirects to ASU library page. The application has been tested and it follows a proper workflow.

CONCLUSION
With this project we provided certain new features into a book recommendation application that would make it easier for users. Incorporating image recognition can lead to much faster searches thus reducing user overheads.

ACKNOWLEDGEMENTS
We are really grateful to Dr. Ayan Banerjee for giving us the opportunity to work on this project as it gave us a valuable experience. We could learn many technologies and concepts in this course and successfully implement them.
