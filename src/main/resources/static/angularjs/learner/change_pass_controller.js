/**
 * Created by MARCOS on 01/02/2017.
 */

app.controller('changePassCtrl', ['$scope', '$http', function($scope, $http){

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
            // Show the empty field error
            $scope.showEmptyFieldsError = true;

            // Show the modal with the error
            $("#errorModal").modal("show");
        }else{
            $http.post('/api/learner/changePassword', $.param($scope.changePasswordData), {
                headers : {
                    "content-type" : "application/x-www-form-urlencoded"
                }
            }).success(function(data) {
                // Check if has an error in the change password process
                if(data.isChanged){
                    // Shows the model with iformation message when the password has been changed successfully
                    $("#successfullyModal").modal("show");
                }else{
                    // Shows the error message in the change password process
                    $scope.showProcessError = true;
                    $scope.processErrorMessage = data.errorMessage;

                    // Show the modal with the error
                    $("#errorModal").modal("show");
                }

                // Clear the change password fields
                $scope.changePasswordData = {};

            }).error(function(data) {
                // Error changing the password, shows the error message
                $scope.showRequestError = true;

                // Show the modal with the error
                $("#errorModal").modal("show");

                // Clear the change password fields
                $scope.changePasswordData = {};
            })
        }
    };

}]);