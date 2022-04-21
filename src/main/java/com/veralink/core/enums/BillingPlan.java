package com.veralink.core.enums;

import javax.persistence.Embeddable;

@Embeddable
public enum BillingPlan {
   STARTER {
      public String toString() {
          return "STARTER";
      }
   },
   PAY_PER_USE {
      public String toString() {
          return "PAY_PER_USE";
      }
   },
   FIXED_MONTHLY {
      public String toString() {
          return "FIXED_MONTHLY";
      }
   }
}
