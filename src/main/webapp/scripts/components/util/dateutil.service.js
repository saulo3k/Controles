'use strict';

angular.module('controlesApp')
    .service('DateUtils', function ($filter) {
      this.convertLocaleDateToServer = function(date) {
        if (date) {
          return $filter('date')(date, 'dd-MMM-yyyy');
        } else {
          return null;
        }
      };
      this.convertLocaleDateFromServer = function(date) {
        if (date) {
          var dateString = date.split("-");
          return new Date(dateString[0], dateString[1] - 1, dateString[2]);
        }
        return null;
      };
      this.convertDateTimeFromServer = function(date) {
        if (date) {
          return new Date(date);
        } else {
          return null;
        }
      }
    });
