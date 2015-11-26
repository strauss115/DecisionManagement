/**
 * service for team information
 */
var teamServices = angular.module('teamServices', ['ngResource']);

teamServices.factory('Teams', ['$resource',
                                   function($resource){
                                     return $resource('http://140.78.186.44:8181/DecisionManagement/rest/team/', {}, {
                                       query: {method:'GET', params:{}, isArray:true}
                                     });
                                   }]);