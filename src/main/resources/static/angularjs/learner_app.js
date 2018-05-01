/**
 * Created by MARCOS on 31/01/2017.
 */

// Angular JS application
var app = angular.module('learnerApp',['ngRoute']);

// Service to get the user profile
app.factory('currentUserProfile',['$http', '$q', function($http, $q) {
    return function() {
        var userProfileData = $http.get('/api/user/getUserProfile');
        return $q.all([userProfileData]).then(function(results){
            return {
                userProfileData: results[0].data
            };
        });
    }
}]);

// Service to access to the shared properties from all controllers
app.service('sharedProperties', function () {
    var currentGroupId = 0;
    var currentExamId = 0;

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
            controller: 'userProfileCtrl',
            resolve: {
                currentProfile : ['currentUserProfile', function (currentUserProfile) {
                    return currentUserProfile();
                }]
            }
        })
        .when('/subject',{
            templateUrl: '/learner/partial/subject',
            controller: 'subjectCtrl'
        })
        .when('/reviewexam',{
            templateUrl: '/learner/partial/reviewExam',
            controller: 'reviewExamCtrl'
        })
        .otherwise({
            redirectTo: '/'
        });
}]);

// Main controller
app.controller('mainCtrl', ['$scope', '$http', '$window', 'sharedProperties', function($scope, $http, $window, sharedProperties){

    // The function to executes when the page has been loaded
    $scope.init = function(){
        // Request to get the full name of user
        $http.get('/api/user/getFullName').success(function(response){
            // Set the full name of user
            $scope.fullName = response.fullName;
        }).error(function(response){
            // Set the full name with "Error" message
            $scope.fullName = 'Error...';
        });
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
app.controller('userProfileCtrl', ['$scope', '$http', '$window', 'currentProfile', 'serverCaller' , function($scope, $http, $window, currentProfile, serverCaller) {
    // The identifier of languages
    const SPANISH_ID = 0;
    const ENGLISH_ID = 1;

    // Select the language button with the current language of user
    if(currentProfile.userProfileData.languageId == SPANISH_ID){
        $("#spanishButton").addClass("active");
    }else if (currentProfile.userProfileData.languageId == ENGLISH_ID){
        $("#englishButton").addClass("active");
    }

    // Function to set the language buttons state
    $scope.setButtonState = function (event) {
        $("#spanishButton").removeClass("active");
        $("#englishButton").removeClass("active");

        var clickedButton = event.currentTarget;

        $("#" + clickedButton.id).addClass("active");
    };

    // Function to get the identifier of selected language button
    var getLanguageIdFromButtons = function () {
        if($("#spanishButton").hasClass("active")){
            return SPANISH_ID;
        }else if($("#englishButton").hasClass("active")){
            return ENGLISH_ID;
        }
    };

    // Function to shows the confirmation modal
    $scope.showConfirmationModal = function () {
        $("#confirmationModal").modal("show");
    };

    // Set the user profile data to shows in the form
    $scope.profile = {};
    $scope.profile.user = currentProfile.userProfileData.username;
    $scope.profile.name = currentProfile.userProfileData.name;
    $scope.profile.lastname = currentProfile.userProfileData.lastName;
    $scope.profile.email = currentProfile.userProfileData.email;
    $scope.profile.dni = currentProfile.userProfileData.dni;

    // Function to update the user profile
    $scope.updateUserProfile = function () {

        var updateUserProfileRequest = {
            name : $scope.profile.name,
            lastName : $scope.profile.lastname,
            email : $scope.profile.email,
            dni : $scope.profile.dni,
            languageId : getLanguageIdFromButtons()
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

    // Function to active the clicked button and deactive the others
    $scope.clickExamsButton = function($event){
        $("#available-exams-button").removeClass("active");
        $("#next-exams-button").removeClass("active");
        $("#done-exams-button").removeClass("active");

        var clickedButton = $event.currentTarget;
        $("#"+clickedButton.id).addClass("active");
    };

    // Functions to check the activated button
    $scope.isAvailableExamsButtonActived = function(){
        return $("#available-exams-button").hasClass("active");
    };
    $scope.isNextExamsButtonActived = function(){
        return $("#next-exams-button").hasClass("active");
    };
    $scope.isDoneExamsButtonActived = function(){
        return $("#done-exams-button").hasClass("active");
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
        $("#confirmationModal").modal("show");
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

    // Get the new exam to perform from the server
    $scope.startExam = function(examId){
        // Prepare te request to get tutor list to send an email
        var getNewExamRequest = {
            examId : examId
        };

        // Call to the server to get the list of tutor to send an email
        serverCaller.httpPost(getTutorsToSendEmailRequest, '/api/learner/getNewExam',
            function (response) {},
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