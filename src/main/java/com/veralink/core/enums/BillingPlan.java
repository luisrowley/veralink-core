package com.veralink.core.enums;

public enum BillingPlan {
   STARTER {
      public String toString() {
          return "Starter";
      }
   },
   PAY_PER_USE {
      public String toString() {
          return "Pay_per_use";
      }
   },
   FIXED_MONTHLY {
      public String toString() {
          return "Fixed_monthly";
      }
   }
}
