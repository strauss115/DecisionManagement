/**
 * service for team information
 */
var teamServices = angular.module('teamServices', ['ngResource']);

teamServices.factory('Teams', ['$resource', 
                                   function($resource, $rootScope){
                                     return $resource(serverAddress + '/DecisionManagement/rest/team/', {}, {
                                       query: {method:'GET', params:{}, isArray:true}
                                     });
                                   }]);