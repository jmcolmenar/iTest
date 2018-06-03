/**
 * Created by MARCOS on 31/01/2017.
 */

// Angular JS application
var app = angular.module('learnerApp',['ngRoute']);

// Service to access to the shared properties from all controllers
app.service('sharedProperties', function () {
    var currentGroupId = 0;
    var currentExamId = 0;
    var newExamInfo = null;
    var examScoreInfo = null;

    return {
        getCurrentGroupId: function () {
            return currentGroupId;
        },
        setCurrentGroupId: function(value) {
            currentGroupId = value;
        },
        getCurrentExamId: function () {
            return currentExamId;
        },
        setCurrentExamId: function(value) {
            currentExamId = value;
        },
        getNewExamInfo: function () {
            return newExamInfo;
        },
        setNewExamInfo: function(value) {
            newExamInfo = value;
        },
        getExamScoreInfo: function () {
            return examScoreInfo;
        },
        setExamScoreInfo: function(value) {
            examScoreInfo = value;
        }
    };
});

// Service to call to the server and manage the response
app.service('serverCaller', function ($http) {
    return{
        // Http post call
        httpPost: function(request, url, succesfullResponse, errorResponse, showErrorModal){
            // Function to check if the error model must be shown
            var checkShowErrorModel = function(showErrorModal, response){
                // Check if shows the error modal
                if(showErrorModal){
                    // Check if the server return an error message
                    if(response && response.errorMessage){
                        // Show the message from the server
                        $("#genericErrorFromServer").text(response.errorMessage).show();
                        $("#genericErrorFromPage").hide();
                    }else{
                        // Show the generic message in the page
                        $("#genericErrorFromServer").hide();
                        $("#genericErrorFromPage").show();
                    }

                    // Show error modal
                    $("#genericErrorModal").modal("show");
                }
            };

            // Call post to the server
            $http.post(url, request, {
                headers : {
                    'content-type' : 'application/json'
                }
            }).success(function(response) {
                // Check if the call return an error
                if(response.hasError){

                    // Execute the error callback
                    errorResponse(response);

                    // Check if shows an error modal
                    checkShowErrorModel(showErrorModal, response);
                }else{
                    // Execute the succes callback
                    succesfullResponse(response);
                }
            }).error(function(response) {
                // Execute the error calling to server callback
                errorResponse(response);

                // Check if shows an error modal
                checkShowErrorModel(showErrorModal, response);
            })
        }
    }
});

// Directive to execute a callback when finish to render "ng-repeat" directive
app.directive('onFinishRender', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            if (scope.$last === true) {
                $timeout(function () {
                    scope.$emit(attr.onFinishRender);
                });
            }
        }
    }
});

// Directive to inject a Geogebra element from file path
app.directive('geogebraElement', function ($timeout) {
    return {
        restrict: 'A',
        link: function (scope, element, attr) {
            $timeout(function () {
                var currentGeogebraElement = scope.$eval(attr.geogebraElement);
                scope.$emit('injectGeogebraElement', currentGeogebraElement);
            });
        }
    }
});

// Route provider configuration
app.config(['$routeProvider', function ($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl: '/learner/partial/courses',
            controller: 'coursesCtrl'
        })
        .when('/changepassword', {
            templateUrl: '/user/partial/changePassword',
            controller: 'changePassCtrl'
        })
        .when('/userprofile',{
            templateUrl: '/user/partial/userProfile',
            controller: 'userProfileCtrl'
        })
        .when('/subject',{
            templateUrl: '/learner/partial/subject',
            controller: 'subjectCtrl'
        })
        .when('/reviewexam',{
            templateUrl: '/learner/partial/reviewExam',
            controller: 'reviewExamCtrl'
        })
        .when('/newexam',{
            templateUrl: '/learner/partial/newExam',
            controller: 'newExamCtrl'
        })
        .when('/examscore',{
            templateUrl: '/learner/partial/examScore',
            controller: 'examScoreCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);

// Main controller
app.controller('mainCtrl', ['$scope', '$http', '$window', '$sce', 'serverCaller', function($scope, $http, $window, $sce, serverCaller){

    // The function to executes when the page has been loaded
    $scope.init = function(){
        // Call to the server to get the full name of user
        serverCaller.httpPost({}, '/api/user/getFullName',
            function (response) {
                // Set the full name of user
                $scope.fullName = response.fullName;
            },
            function (response) {
                // Set the full name with "Error" message
                $scope.fullName = 'Error...';
            },
            true);
    };

    // Function to redirect to learner home page
    $scope.goToLearnerIndexPage = function () {
        // The learner is redirected to the home page
        $window.location.href = '/learner/'
    };

    // Call to "logout" get request to exit of application
    $scope.exitApplication = function(){
        // Hide confirmation modal
        $("#exitConfirmationModal").modal("hide");

        // Redirect to "Logout" url
        $window.location.href = '/logout'
    };

    // Show the modal with the exit confirmation
    $scope.showExitConfirmation = function(){
        $("#exitConfirmationModal").modal("show");
    };

    // This function is used to convert and draw the formulas using the AsciiMath library
    $scope.$on('convertFormulas', function(ngRepeatFinishedEvent) {
        // Translate the formulas through Ascii Math javascript library
        asciimath.translate();
    });
}]);

// Multimedia controller
app.controller('multimediaCtrl', ['$scope', '$sce', function($scope, $sce){

    // Event to inject the geogebra element
    $scope.$on('injectGeogebraElement', function(event, multimedia) {
        var applet = new GGBApplet({filename: multimedia.path,"showtoolbar":true, "showFullscreenButton":true }, true);
        applet.inject('geogebra'+multimedia.id);
    });

    // Function to get the url to use in an embed element
    $scope.getYoutubeUrl = function(path){
        return $sce.trustAsResourceUrl(path);
    };

    // Get the audio type for the <audio> html5 tag from file extension
    $scope.getAudioType = function(multimedia){
        if(multimedia.extension == 'mp3'){
            return 'mpeg';
        }else if(multimedia.extension == 'wav'){
            return 'wav';
        }else if(multimedia.extension == 'ogg'){
            return 'ogg';
        }else{
            return 'mpeg';
        }
    };

    // Function to show an image in a modal
    $scope.showImageInModal = function (src, $event) {
        // Set the source of image and show the modal
        $scope.imageSrc = src;
        $("#showImageModal").modal("show");

        // Prevent the event to avoid checking the answer
        if($event != null){
            $event.preventDefault();
        }
    };

    // Get the style for multimedia question
    $scope.getStyleForMultimediaQuestion = function(multimedia){
        var style = '';

        // Check the multimedia element is an image
        if(multimedia.type == 2){
            // Calculate Width style
            if(multimedia.measureUnitWidth == 2){

                // The measure unit is "PIXEL"
                style += 'width:' + multimedia.width + 'px;';

            }else if(multimedia.measureUnitWidth == 3){

                // The measure unit is "PERCENT"
                style += 'width:' + multimedia.width + ';';

            }

            // Calculate Height style
            if(multimedia.measureUnitHeight == 2){

                // The measure unit is "PIXEL"
                style += 'height:' + multimedia.height + 'px;';

            }else if(multimedia.measureUnitHeight == 3){

                // The measure unit is "PERCENT"
                style += 'height:' + multimedia.height + ';';

            }
        }

        // Return the style
        return style;
    };

    $scope.getClassForMultimediaQuestion = function(multimedia){
        var className = "";
        switch (multimedia.type){
            case 1:
                className = " multimedia-flash-question";
                break;
            case 2:
                if(multimedia.measureUnitWidth == 0 && multimedia.measureUnitHeight == 0){

                    // The measure unit is "NONE", therefore the size will be the same as "small"
                    className = " multimedia-img-small-question";

                }else if(multimedia.measureUnitWidth == 1){

                    // The measure unit is "SIZE"
                    if(multimedia.width == 'big'){

                        // The size is "big"
                        className = " multimedia-img-big-question";

                    }else if(multimedia.width == 'medium'){

                        // The size is "medium"
                        className = " multimedia-img-medium-question";

                    }else if(multimedia.width == 'small') {

                        // The size is "small"
                        className = " multimedia-img-small-question";
                    }
                }
                break;
            case 3:
                className = " multimedia-audio";
                break;
            case 7:
                className = " multimedia-youtube-question";
                break;
            default:
                className = "";
        }

        return "multimedia-element-question-div" + className;
    };

    // Function to get the class for the multimedia comment element
    $scope.getClassForMultimediaComment = function(multimedia){
        var className;
        switch (multimedia.type){
            case 1:
                className = " multimedia-flash-comment";
                break;
            case 2:
                className = " multimedia-img-comment";
                break;
            case 3:
                className = " multimedia-audio-comment";
                break;
            case 7:
                className = " multimedia-youtube-comment";
                break;
            default:
                className = "";
        }

        return "multimedia-element-comment-div" + className;
    };
}]);

// Change password controller
app.controller('coursesCtrl', ['$scope', '$http', 'sharedProperties', 'serverCaller', function($scope, $http, sharedProperties, serverCaller) {

    // Call to the server to get the courses list of user
    serverCaller.httpPost({}, '/api/learner/getCourses',
        function (response) {
            // Set the variable with the courses
            $scope.courseList = response.coursesList;
        },
        function (response) {
            // Set an empty courses list
            $scope.courseList = {};
        },
        true);

    // Set the selected subject
    $scope.setSelectedSubject = function(subject){
        // Set the GroupId of selected subject to search the exams
        sharedProperties.setCurrentGroupId(subject.groupId);
    };

}]);

// Change password controller
app.controller('changePassCtrl', ['$scope', '$http', 'serverCaller', function($scope, $http, serverCaller){

    // The change password data
    $scope.changePasswordData = {};

    // Function to perform login
    $scope.changePassword = function() {
        // For default he errors changing the password are not shown
        $scope.showEmptyFieldsError = false;
        $scope.showRequestError = false;
        $scope.showProcessError = false;

        // Check the all fields are not null or empty
        if(!$scope.changePasswordData.oldPassword || !$scope.changePasswordData.newPassword || !$scope.changePasswordData.repeatPassword){
            // Show the modal with the error when any field is empty
            $("#errorEmptyFieldModal").modal("show");
        }else{
            // Prepare the request
            var changePasswordRequest = {
                oldPassword : $scope.changePasswordData.oldPassword,
                newPassword : $scope.changePasswordData.newPassword,
                repeatPassword : $scope.changePasswordData.repeatPassword
            };
            // Call to the server to chage the user password
            serverCaller.httpPost(changePasswordRequest, '/api/user/changePassword',
                function (response) {
                    // Shows the model with iformation message when the password has been changed successfully
                    $("#successfullyChangePasswordModal").modal("show");
                },
                function (response) {},
                true);
        }
    };
}]);

// User profile controller
app.controller('userProfileCtrl', ['$scope', '$http', '$window', 'serverCaller' , function($scope, $http, $window, serverCaller) {
    // The language identifiers
    $scope.spanishId = 0;
    $scope.englishId = 1;
    $scope.frenchId = 2;

    // Call to the server to get user profile data
    serverCaller.httpPost({}, '/api/user/getUserProfile',
        function (response) {
            // Set the user profile data from the server
            $scope.profile = {};
            $scope.profile.user = response.username;
            $scope.profile.name = response.name;
            $scope.profile.lastname = response.lastName;
            $scope.profile.email = response.email;
            $scope.profile.dni = response.dni;
            $scope.profile.languageId = response.languageId;
        },
        function (response) {},
        true);

    // Function to shows the confirmation modal to update user profile
    $scope.showConfirmationModal = function () {
        $("#updateProfileConfirmationModal").modal("show");
    };

    // Function to update the user profile
    $scope.updateUserProfile = function () {

        var updateUserProfileRequest = {
            name : $scope.profile.name,
            lastName : $scope.profile.lastname,
            email : $scope.profile.email,
            dni : $scope.profile.dni,
            languageId : $scope.profile.languageId
        };

        // Call to the server to to update the user profile
        serverCaller.httpPost(updateUserProfileRequest, '/api/user/updateUserProfile',
            function (response) {
                // Shows the static modal with successful message
                $("#successfullyModal").modal({backdrop: "static"});

                // Set the translated success message
                $scope.successMessageUpdatingProfile = response.successMessage;
            },
            function (response) {},
            true);
    };
}]);

// Subject management controller
app.controller('subjectCtrl', ['$scope', '$http' , '$window', 'sharedProperties', 'serverCaller', function($scope, $http, $window, sharedProperties, serverCaller){

    // Prepare te request to get the exams by the Group Id of selected subject
    var getExamsInfoRequest = {
        groupId : sharedProperties.getCurrentGroupId()
    };

    // Call to the server to get the exams info
    serverCaller.httpPost(getExamsInfoRequest, '/api/learner/getExamsInfo',
        function (response) {
            // Set the list of done exams
            $scope.subject = response.subject;
            $scope.availableExamsList = response.availableExamsList;
            $scope.nextExamsList = response.nextExamsList;
            $scope.doneExamsList = response.doneExamsList;
        },
        function (response) {
            // Set empty subject and exams info
            $scope.subject = {};
            $scope.availableExamsList = {};
            $scope.nextExamsList = {};
            $scope.doneExamsList = {};
        },
        true);

    // Variables and functions to show the exam category
    $scope.selectedCategory = 0;
    $scope.availableExamsCategory = 0;
    $scope.nextExamsCategory = 1;
    $scope.doneExamsCategory = 2;
    $scope.isSelectedCategory = function (category){
        return $scope.selectedCategory == category;
    };
    $scope.setSelectedCategory = function(category){
        $scope.selectedCategory = category;
    };

    // Function to set the current exam in order to show the extra info in the modal
    $scope.setCurrentExamExtraInfo = function (exam) {
        $scope.currentExamExtraInfo = exam;
    };

    // Function to shows the confirmation modal to go to the review exam
    $scope.showGoToReviewExamModal = function (examId) {
        // Set the exam identifier
        sharedProperties.setCurrentExamId(examId);

        // Show the confirmation modal
        $("#goToReviewConfirmationModal").modal("show");
    };

    // Function to go to exam review
    $scope.goToExamReview = function () {
        // The learner is redirected to the review exam page
        $window.location.href = '#/reviewexam/';
    };

    $scope.getTutorsToSendEmail = function(){
        // Prepare te request to get tutor list to send an email
        var getTutorsToSendEmailRequest = {
            groupId : $scope.subject.groupId
        };

        // Call to the server to get the list of tutor to send an email
        serverCaller.httpPost(getTutorsToSendEmailRequest, '/api/learner/getTutorsToSendEmail',
            function (response) {
                // Set the tutor info list in a variable
                $scope.tutorsToSendEmail = response.tutorInfoList;

                // Creates the scope variable of selected tutor
                $scope.selectedTutor = {
                    email : ''
                };

                // Selected the first tutor email in the list
                if($scope.tutorsToSendEmail != null && $scope.tutorsToSendEmail.length > 0){
                    $scope.selectedTutor.email = $scope.tutorsToSendEmail[0].email;
                }

                // Open the modal with the list of tutors
                $('#sendEmailModal').modal('show');
            },
            function (response) {},
            true);
    };

    // Function to open confirmation modal to perform a new exam
    $scope.showGoToNewExam = function (examId) {

        // Set the id of new exam to perform
        $scope.newExamId = examId;

        // Open the confirmation modal
        $('#goToNewExamConfirmationModal').modal('show');
    };


    // Get the new exam to perform from the server
    $scope.startExam = function(){
        // Prepare te request to get the new exam to perform
        var getNewExamRequest = {
            examId : $scope.newExamId
        };

        // Call to the server to get the new exam to perform
        serverCaller.httpPost(getNewExamRequest, '/api/learner/getNewExam',
            function (response) {
                // Set the new exam info in the shared properties
                sharedProperties.setNewExamInfo(response.newExam);

                // Go to the new exam page
                $window.location.href = '#/newexam/';
            },
            function (response) {},
            true);
    };

}]);

// Exam to review management controller
app.controller('reviewExamCtrl', ['$scope', '$http', 'sharedProperties', 'serverCaller', function($scope, $http, sharedProperties, serverCaller){

    // Prepare te request to get the exam to review
    var getExamToReviewRequest = {
        examId : sharedProperties.getCurrentExamId()
    };


    // Call to the server to get the exam to review of selected subject
    serverCaller.httpPost(getExamToReviewRequest, '/api/learner/getExamToReview',
        function (response) {
            // Get the exam to review information from response
            $scope.examToReview = {};
            $scope.examToReview.subjectName = response.subjectName;
            $scope.examToReview.examTitle = response.examTitle;
            $scope.examToReview.score = response.score;
            $scope.examToReview.maxScore = response.maxScore;
            $scope.examToReview.questionList = response.questionList;
        },
        function (response) {
            // Set empty review information
            $scope.examToReview = {};
            $scope.examToReview.questionList = {};
        },
        true);
}]);

// New exam management controller
app.controller('newExamCtrl', ['$scope', '$http', '$window', '$interval', 'sharedProperties', 'serverCaller', function($scope, $http, $window, $interval, sharedProperties, serverCaller){

    // Function to convert a date in milliseconds to string
    var convertDateToString = function(date){
        // Time calculations for days, hours, minutes and seconds
        var hours = Math.floor((date % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        var minutes = Math.floor((date % (1000 * 60 * 60)) / (1000 * 60));
        var seconds = Math.floor((date % (1000 * 60)) / 1000);

        // Form the string and return
        return (hours < 10 ? '0' : '') + hours + ":" + (minutes < 10 ? '0' : '') + minutes + ":" + (seconds < 10 ? '0' : '') + seconds;
    };

    // Function to start the remaining time in the countdown timer
    $scope.startCountdownTimer = function(){

        // Create an interval
        var finishTime = new Date().getTime() + ($scope.newExam.examTime * 60 * 1000) + 1000;
        $scope.timeExamInterval = $interval(function() {

            // Get todays date and time and get the date diffence
            var now = new Date().getTime();
            var remaining = finishTime - now;

            // Set the variable with the remaining time
            $scope.remainingTime = convertDateToString(remaining);

            // If the count down is finished, execute the finish exam function
            if (remaining < 0) {
                // Clear the interval
                $interval.cancel($scope.timeExamInterval);
                $scope.remainingTime = convertDateToString(0);

                // Finish the exam
                $scope.endExam(false);
            }
        }, 200);
    };

    // Get the new exam info and set empty exam in shared properties
    $scope.newExam = sharedProperties.getNewExamInfo();
    sharedProperties.setNewExamInfo(null);

    // Check if there is a new exam to perform
    if(!$scope.newExam){
        // Go to the error page
        $window.location.href = '/error/';
    }else{
        // Start the coundown timer
        $scope.remainingTime = convertDateToString($scope.newExam.examTime * 60 * 1000);
        $scope.startCountdownTimer();
    }

    // Function to check if the CheckBox can be changed due to the maximum number of right answers
    $scope.checkChangedAnswer = function (question, answer) {

        // Check if the exam shows the number of right answers
        if($scope.newExam.showNumberRightAnswers){
            // Get the number of checked answers
            var checkedAnswersCounter = 0;
            question.answerList.forEach(function(answerItem) {
                checkedAnswersCounter += answerItem.checked ? 1 : 0;
            });

            if(checkedAnswersCounter > question.numberCorrectAnswers){
                // Show warning modal to indicate that no more answers can be checked
                $('#numberRighAnswersModal').modal('show');

                // Set the asnwer as not checked
                answer.checked = false;
            }else{
                // Set the answer time for this answer
                answer.answerTime = new Date().getTime();
            }
        }else{
            // Set the answer time for this answer
            answer.answerTime = new Date().getTime();
        }
    };

    // Function to end the exam
    $scope.endExam = function(isFromEndExamButton){
        // Variable to check if call to the server to
        var callServerToEndExam = true;

        // Check if the exam button is clicked
        if(isFromEndExamButton){

            // Check if all questions are answered
            var allQuestionsAreAnswered = true;
            $scope.newExam.questionList.forEach(function (question) {
                checkedAnswers = question.answerList.filter(function(answer){ return answer.checked; });
                if(checkedAnswers.length == 0) { allQuestionsAreAnswered = false; }
            });

            // Show error modal when all questions are not answered
            if(!allQuestionsAreAnswered){
                $("#allQuestionsNotAnsweredModal").modal("show");

                // It will not call to the server because there are questions not answered
                callServerToEndExam = false;
            }
        }

        // Check if call to the server to
        if(callServerToEndExam){
            // Shows a confirmation model when the exam is ended from the finish button
            if(isFromEndExamButton){
                $("#finishExamModal").modal("show");
            }else{
                // Prepare te request to end the exam
                var endExamRequest = {
                    examId : $scope.newExam.examId,
                    questionList : $scope.newExam.questionList
                };

                // Call to the server to end the exam
                serverCaller.httpPost(endExamRequest, '/api/learner/endExam',
                    function (response) {
                        // Clear event to avoid exiting of new exam page
                        windowElement.off('beforeunload', preventEventFunction);

                        // Set the exam score response in the shared properties
                        sharedProperties.setExamScoreInfo(response.examScoreInfo);

                        // Go to the exam score info page
                        $window.location.href = '#/examscore/';
                    },
                    function (response) {},
                    true);
            }
        }
    };

    // Event to show a popup before refresh/close the tab in the browser
    var preventEventFunction = function (event) { event.preventDefault(); };
    var windowElement = angular.element($window);
    windowElement.on('beforeunload', preventEventFunction);

    // Event to check if the back button is clicked
    $scope.$on('$routeChangeStart', function (event, next, prev) {

        // Check if the back button is clicked to avoid going to the previous page
        if(next.controller != null && next.controller == 'subjectCtrl'){
            // Prevent the event
            event.preventDefault();

            // End the exam as the same that End Exam button
            $scope.endExam(true);
        }
    });

}]);

// Exam score management controller
app.controller('examScoreCtrl', ['$scope', '$window', 'sharedProperties', 'serverCaller', function($scope, $window, sharedProperties, serverCaller){

    // Get the exam score info from the shared properties
    $scope.examScoreInfo = sharedProperties.getExamScoreInfo();

    // Function to go to exam review
    $scope.goToReview = function () {
        // Set the exam identifier
        sharedProperties.setCurrentExamId($scope.examScoreInfo.examId);

        // The learner is redirected to the review exam page
        $window.location.href = '#/reviewexam/';
    };

}]);