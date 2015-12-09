/**
 * service for user information
 */
var userServices = angular.module('userServices', ['ngResource']);

userServices.factory('Users', ['$resource',
                                   function($resource){
                                     return $resource('http://192.168.0.102:8181/DecisionManagement/rest/user/', {}, {
                                       query: {method:'GET', params:{eMail:'user1@u1.com'}, isArray:true}
                                     });
                                   }]);

