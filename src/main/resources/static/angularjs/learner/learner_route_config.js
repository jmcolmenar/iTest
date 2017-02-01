/**
 * Created by MARCOS on 31/01/2017.
 */
app.config(function ($routeProvider){
    $routeProvider
        .when('/', {
            templateUrl: '/learner/partial/courses'
        })
        .when('/changepassword', {
            templateUrl: '/learner/partial/changePassword'
        })
        .when('/userprofile',{
            templateUrl: '/learner/partial/userProfile'
        });
});